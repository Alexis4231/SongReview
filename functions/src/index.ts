import * as functions from "firebase-functions/v2";
import * as admin from "firebase-admin";
import fetch from "node-fetch";
import * as logger from "firebase-functions/logger";
import {onSchedule} from "firebase-functions/v2/scheduler";

admin.initializeApp();


export const deleteUnverifiedUsers =
  onSchedule("every 24 hours", async () => {
    const result = await admin.auth().listUsers();
    const now = Date.now();

    const deletionPromises = result.users.map(async (userRecord) => {
      const emailVerified = userRecord.emailVerified;
      const creationTime = userRecord.metadata.creationTime;
      const email = userRecord.email;

      if (!creationTime) return;

      const createdAt = new Date(creationTime).getTime();
      const ageInHours = (now - createdAt) / (1000 * 60 * 60);

      if (!emailVerified && ageInHours > 24) {
        await admin.auth().deleteUser(userRecord.uid);
        const snapshot = await admin.firestore().collection("users")
          .where("email", "==", email)
          .get();
        if (!snapshot.empty) {
          const doc = snapshot.docs[0];
          await doc.ref.delete();
        }
      }
    });

    await Promise.all(deletionPromises);
  });

export const getSpotifyToken = functions.https.onRequest(async (req, res) => {
  const spotifyClientId = functions.config().spotify.client_id;
  const spotifyClientSecret = functions.config().spotify.client_secret;

  // Crear el token de autorización
  const auth = Buffer.from(
    `${spotifyClientId}:${spotifyClientSecret}`
  ).toString("base64");

  try {
    const response = await fetch("https://accounts.spotify.com/api/token", {
      method: "POST",
      headers: {
        // Corregir la interpolación aquí
        "Authorization": `Basic ${auth}`,
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: "grant_type=client_credentials",
    });

    if (!response.ok) {
      logger.error("Failed to fetch token from Spotify", {
        status: response.status,
        statusText: response.statusText,
      });
      res.status(500).json({error: "Spotify token request failed"});
      return;
    }

    let data: any;
    try {
      data = await response.json();
    } catch (jsonError) {
      logger.error("Failed to parse response JSON from Spotify", jsonError);
      res.status(500).json({error: "Failed to parse JSON response"});
      return;
    }

    if (data.access_token && data.token_type && data.expires_in) {
      res.status(200).json({access_token: data.access_token});
    } else {
      logger.error("Spotify auth failed", data);
      res.status(500)
        .json({error: "Failed to get access token", details: data});
    }
  } catch (error) {
    logger.error("Spotify token error", error);
    res.status(500).json({error: "Internal server error"});
  }
});
