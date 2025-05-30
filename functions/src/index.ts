import { onSchedule } from "firebase-functions/v2/scheduler";
import { onDocumentCreated } from "firebase-functions/v2/firestore";
import { onDocumentUpdated } from "firebase-functions/v2/firestore";
import { logger } from "firebase-functions";
import * as admin from "firebase-admin";

// Inicialización de Firebase Admin SDK
admin.initializeApp();

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

// notificaciones
export const sendFriendRequestNotification = onDocumentCreated("requests/{requestId}", async (event) => {
    try{
        const snapshot = event.data;
        if (!snapshot){
            logger.error("No se encontro el documento de la solicitud");
            return;
        }
        const request = snapshot.data();
        if (!request || !request.codeIssuer || !request.codeReceiver){
            logger.error("Datos incompletos en la solicitud",request);
            return;
        }
        const fromUserDoc = await admin.firestore().collection("users").doc(request.codeIssuer).get();
        const toUserDoc = await admin.firestore().collection("users").doc(request.codeReceiver).get();
        if(!fromUserDoc.exists || !toUserDoc.exists){
            logger.error("Uno de los usuarios no existe");
            return;
        }
        const fromUser = fromUserDoc.data();
        const toUser = toUserDoc.data();
        const fcmToken = toUser?.fcmToken;
        if (!fcmToken){
            logger.warn("El usuario receptor no tiene un token FCM");
            return;
        }
        const fromName = fromUser?.name || "Un usuario";
        const message = {
            notification: {
                title: "Nueva solicitud de amistad",
                body: `${fromName} te ha enviado una solicitud de amistad`,
            },
            data: {
                username: fromName,
                receiverUid: request.codeReceiver,
            },
            token: fcmToken,
        };
        await admin.messaging().send(message);
        logger.info(`Notificacion de peticion enviada a ${toUser?.email || 'receptor desconocido'}`);
    }catch(error){
        logger.error("Error al enviar la notificacion de solicitud de amistad:",error);
    }
});

export const sendFriendAcceptedNotification = onDocumentUpdated("requests/{requestId}", async (event) => {
    try{
        const beforeData = event.data?.before?.data();
        const afterData = event.data?.after?.data();

        if(!beforeData || !afterData){
            logger.error("Error al obtener los datos de la actualizacion");
            return;
        }

        if(beforeData.status === "PENDING" && afterData.status === "ACCEPTED"){
            const fromUserDoc = await admin.firestore().collection("users").doc(afterData.codeIssuer).get();
            const toUserDoc = await admin.firestore().collection("users").doc(afterData.codeReceiver).get();
            if(!fromUserDoc.exists || !toUserDoc.exists){
                logger.error("Uno de los usuarios no existe");
                return;
            }
            const fromUser = fromUserDoc.data();
            const toUser = toUserDoc.data();
            const fcmToken = fromUser?.fcmToken;
            if(!fcmToken){
                logger.warn("El usuario receptor no tiene el un token FCM");
                return;
            }
            const fromName = toUser?.name || "Un usuario";
            const message = {
                notification:{
                    title: "Solicitud de amistad aceptada",
                    body: `${fromName} ha aceptado tu solicitud de amistad`,
                },
                data: {
                    username: fromName,
                    receiverUid: afterData.codeReceiver,
                },
                token: fcmToken,
            };
            await admin.messaging().send(message);
            logger.info(`Notificacion de aceptacion enviada a ${fromUser?.email || 'receptor desconocido'}`);
        }
    }catch(error){
        logger.error("Error al enviar la notificacion de aceptacion de amistad:",error);
    }
});
