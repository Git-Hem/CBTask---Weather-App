package com.cricbuzztask.weatherapp.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Locations : Screen("locations")
}