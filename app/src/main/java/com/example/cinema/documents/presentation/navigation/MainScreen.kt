package com.example.cinema.documents.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cinema.documents.domain.OnboardingPreferences
import com.example.cinema.documents.presentation.screen.account.AccountScreen
import com.example.cinema.documents.presentation.screen.home.HomeScreen
import com.example.cinema.documents.presentation.screen.moviedetail.MovieDetailScreen
import com.example.cinema.documents.presentation.screen.onboarding.OnboardingScreen
import com.example.cinema.documents.presentation.screen.search.SearchScreen
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentDestination = navController
        .currentBackStackEntryFlow
        .collectAsState(initial = navController.currentBackStackEntry)
        .value?.destination?.route ?: ""

    val showBottomBar = currentDestination in listOf(
        Screen.Home.route,
        Screen.Search.route,
        Screen.Account.route
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferences = remember { OnboardingPreferences(context) }

    val onboardingPreferences = OnboardingPreferences(navController.context)
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(
                    currentDestination = currentDestination,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.OnBoardingScreen.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    onboardingPreferences = onboardingPreferences
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    navController = navController,
                    onFilterClick = {},
                )
            }
            composable(Screen.Account.route) { AccountScreen(navController) }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("filmId") { type = NavType.IntType })
            ) { backStackEntry ->
                val filmId = backStackEntry.arguments?.getInt("filmId") ?: 0
                MovieDetailScreen(
                    filmId = filmId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }
            composable(
                route = Screen.OnBoardingScreen.route
            ) {
                OnboardingScreen {
                    scope.launch {
                        preferences.setOnboardingCompleted()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.OnBoardingScreen.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}
