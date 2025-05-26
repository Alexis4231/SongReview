import {onSchedule} from "firebase-functions/v2/scheduler";
import * as logger from "firebase-functions/logger";
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
