import * as functions from "firebase-functions/v2";
import { onSchedule } from "firebase-functions/v2/scheduler";
import * as logger from "firebase-functions/logger";
import admin from "firebase-admin";
import fetch from "node-fetch"; // Usa node-fetch@2
import { Request, Response } from "express"; // Mejores tipos para HTTPS functions

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

// Interfaz para la respuesta de Spotify
interface SpotifyTokenResponse {
    access_token: string;
    token_type: string;
    expires_in: number;
}

// Función HTTPS para obtener el token de Spotify
export const getSpotifyToken = functions.https.onRequest(async (req: Request, res: Response) => {
    const spotifyClientId = functions.config().spotify.client_id;
    const spotifyClientSecret = functions.config().spotify.client_secret;

    const auth = Buffer.from(`${spotifyClientId}:${spotifyClientSecret}`).toString("base64");

    try {
        const response = await fetch("https://accounts.spotify.com/api/token", {
            method: "POST",
            headers: {
                "Authorization": `Basic ${auth}`,
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: "grant_type=client_credentials",
        });

        if (!response.ok) {
            logger.error("Falló la solicitud de token a Spotify", {
                status: response.status,
                statusText: response.statusText,
            });
            res.status(500).json({ error: "Error en la solicitud de token de Spotify" });
            return;
        }

        const data = await response.json() as SpotifyTokenResponse;

        if (data.access_token) {
            res.status(200).json({ access_token: data.access_token });
        } else {
            logger.error("Error de autenticación en Spotify", data);
            res.status(500).json({
                error: "No se pudo obtener el token de acceso",
                details: data,
            });
        }
    } catch (error) {
        logger.error("Error en la solicitud de token de Spotify", error);
        res.status(500).json({ error: "Error interno del servidor" });
    }
});
