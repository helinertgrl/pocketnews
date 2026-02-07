package com.example.pocketnews.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pocketnews.data.local.PreferencesManager
import com.example.pocketnews.presentation.home.HomeScreen
import com.example.pocketnews.presentation.onboarding.OnboardingScreen
import com.example.pocketnews.presentation.settings.SettingsScreen

sealed class Screen(val route: String){
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Settings : Screen("settings")
}

@Composable
fun Navigation(
    preferencesManager: PreferencesManager
){

    val navController = rememberNavController()
    val isFirstLaunch by preferencesManager.isfirstlaunchflow.collectAsState(initial = null)

    if (isFirstLaunch == null) {
        return
    }

    val startDest = if (isFirstLaunch == true) Screen.Onboarding.route else Screen.Home.route


    NavHost(
        navController = navController,
        startDestination = startDest ){

        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }

        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}