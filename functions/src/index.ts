import { onSchedule } from "firebase-functions/v2/scheduler";
import { onCall } from "firebase-functions/v2/https";
import { onDocumentCreated } from "firebase-functions/v2/firestore";
import * as logger from "firebase-functions/logger";
import * as admin from "firebase-admin";

// Inicialización de Firebase Admin SDK
admin.initializeApp({
    credential: admin.credential.cert(require("../serviceAccountKey.json"))
});

// Función programada para borrar usuarios no verificados después de 24h
export const deleteUnverifiedUsers = onSchedule("every 24 hours", async () => {
    try {
        let nextPageToken: string | undefined;
        do {
            const result = await admin.auth().listUsers(1000, nextPageToken);
            const now = Date.now();

            const deletionPromises = result.users.map(async (userRecord) => {
                const emailVerified = userRecord.emailVerified;
                const creationTime = userRecord.metadata.creationTime;
                const email = userRecord.email;

                if (!creationTime) return;

                const createdAt = new Date(creationTime).getTime();
                const ageInHours = (now - createdAt) / (1000 * 60 * 60);

                if (!emailVerified && ageInHours > 24 && email) {
                    try {
                        await admin.auth().deleteUser(userRecord.uid);
                        logger.info(`Usuario eliminado: ${email}`);

                        const snapshot = await admin.firestore()
                            .collection("users")
                            .where("email", "==", email)
                            .get();

                        if (!snapshot.empty) {
                            const doc = snapshot.docs[0];
                            await doc.ref.delete();
                            logger.info(`Documento de Firestore eliminado para el usuario: ${email}`);
                        }
                    } catch (error) {
                        logger.error(`Error al eliminar el usuario ${email}:`, error);
                    }
                }
            });

            await Promise.all(deletionPromises);
            nextPageToken = result.pageToken;
        } while (nextPageToken);
    } catch (error) {
        logger.error("Error general al eliminar usuarios no verificados:", error);
    }
});

// envio de notificaciones
export const sendRequestNotification = onCall(async (request) => {
    const { token, senderName } = request.data
    if(!token || !senderName){
        logger.warn("Datos faltantes en la solicitud");
        throw new Error("token y senderName son requeridos");
    }

    const message = {
        notification: {
            title: "Nueva solicitud de amistad",
            body: `${senderName} te ha enviado una solicitud de amistad`
        },
        token: token,
    };

    try{
        const response = await admin.messaging().send(message);
        logger.info("Notificacion enviada:",response);
        return { success: true }
    }catch(error: any){
        logger.error("Error al enviar la notificacion:",error);
        return { success: false, error: error.message }
    }
    });

export const notifyRequestCreated = onDocumentCreated("requests/{requestId}", async (event) =>{
        const requestData = event.data?.data();
        if(!requestData){
            logger.warn("No se encontro la solicitud");
            return;
        }
        const codeIssuer = requestData.codeIssuer;
        const codeReceiver = requestData.codeReceiver;

        try{
            const issuerSnap = await admin.firestore().collections("users").doc(codeIssuer).get();
            const issuerName = issuerSnap.exists ? issuerSnap.data()?.name : "";
            const receiverSnap = await admin.firestore().collection("users").doc(codeReceiver).get();
            const token = receiverSnap.exists ? receiverSnap.data()?.fcmToken : null;

            if(!token){
                logger.warn("No se encontro token de FCM para el receptor: ${codeReceiver}");
                return;
            }
            const response = await admin.messaging().send(message);
            logger.info("Notificacion enviada:",response);
        }catch(error){
            logger.error("Error al enviar la notificacion",error)
        }
    }
);