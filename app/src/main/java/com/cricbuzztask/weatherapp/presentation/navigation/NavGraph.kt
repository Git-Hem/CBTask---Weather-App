package com.cricbuzztask.weatherapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cricbuzztask.weatherapp.presentation.home.HomeScreen
import com.cricbuzztask.weatherapp.presentation.home.HomeViewModel
import com.cricbuzztask.weatherapp.presentation.locations.LocationsScreen
import com.cricbuzztask.weatherapp.presentation.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                viewModel = viewModel,
                onNavigateToLocations = {
                    navController.navigate(Screen.Locations.route)
                }
            )
        }

        composable(route = Screen.Locations.route) {
            val parentEntry = navController.getBackStackEntry(Screen.Home.route)
            val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)

            LocationsScreen(
                onNavigateBack = { navController.navigateUp() },
                onCitySelected = { cityName ->
                    homeViewModel.loadWeather(cityName)
                    navController.navigateUp()
                }
            )
        }
    }
}