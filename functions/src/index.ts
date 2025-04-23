import {onSchedule} from "firebase-functions/v2/scheduler";
import * as admin from "firebase-admin";

admin.initializeApp();

export const deleteUnverifiedUsers = onSchedule("every 24 hours", async () => {
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
