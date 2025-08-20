package com.example.cinema.documents.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cinema.documents.presentation.screen.account.AccountScreen
import com.example.cinema.documents.presentation.screen.home.HomeScreen
import com.example.cinema.documents.presentation.screen.moviedetail.MovieDetailScreen
import com.example.cinema.documents.presentation.screen.search.SearchScreen

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
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
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
        }
    }
}
