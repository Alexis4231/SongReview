# 🎵 SongReview

This project consists of developing an Android application for song reviews using **Kotlin** and **Jetpack Compose**, with **Firebase** as the backend.  
The app allows users to discover, rate, and comment on songs categorized by musical genre, promoting interaction between listeners and artists.

Each song has its own information sheet that includes title, artist, genre, and streaming links to major platforms such as **Spotify**, **Deezer**, and **YouTube**.  
Registered users can post reviews, rate songs, and follow each other to see mutual reviews and opinions.

---

## 🧰 Main technologies
- **Kotlin**  
- **Jetpack Compose**  
- **Firebase** (Authentication, Firestore, App Check, Cloud Functions, Cloud Messaging, Hosting)  
- **Retrofit** (API consumption for Spotify, Deezer, YouTube)  
- **ExoPlayer** (audio playback)

---

## ⚙️ Key features
- User registration and login with **Firebase Authentication**  
- Secure data storage in **Firestore Database**  
- Song search by title, artist, or genre  
- **CRUD operations** on reviews  
- Real-time notifications via **Firebase Cloud Messaging**  
- **OAuth 2.0** integration with Spotify through a secure web service  
- Friendly and responsive UI built with **Jetpack Compose**  
- **MVVM** architecture separating View, ViewModel, and Model layers

---

## 🎯 Objective
To create a social and collaborative space where music lovers can share their opinions, discover new songs, and provide artists with real feedback that fosters artistic growth.

---

## 🧱 Architecture
The app follows the **MVVM (Model–View–ViewModel)** pattern.  

- **View:** Built with Jetpack Compose (declarative UI).  
- **ViewModel:** Handles logic, state management, and data coordination.  
- **Model:** Firebase Firestore manages real-time operations with a secure, scalable NoSQL structure.

---

## 🧪 Testing
Functional, black-box, and acceptance tests were conducted to ensure stability, UI responsiveness, and error handling.  
The app was validated by real users and achieved consistent, reliable performance under different conditions.
