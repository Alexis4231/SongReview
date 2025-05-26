package com.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Login: Screen("login")
    data object Register: Screen("register")
    data object SuccesRegister: Screen("successregister")
    data object Home: Screen("home")
    data object Add: Screen("add")
    data object Reviews: Screen("reviews/{songCode}"){
        fun createRoute(songCode: String) = "reviews/$songCode"
    }
    data object Profile: Screen("profile")
    data object ResetPassword: Screen("resetpassword")
    data object SuccessResetPassword: Screen("successresetpassword")
    data object SongAdded: Screen("songadded/{songName}-{songArtist}"){
        fun createRoute(songName: String, songArtist: String) = "songadded/$songName-$songArtist"
    }
    data object SongNotAdded: Screen("songnotadded/{songName}-{songArtist}"){
        fun createRoute(songName: String, songArtist: String) = "songnotadded/$songName-$songArtist"
    }
    data object Splash: Screen("splash")
    data object CardUser: Screen("carduser/{username}"){
        fun createRoute(username: String) = "carduser/$username"
    }
    data object Followers: Screen("followers")

    data object Prueba: Screen("prueba")
}