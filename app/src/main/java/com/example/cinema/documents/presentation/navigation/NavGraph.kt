package com.example.cinema.documents.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cinema.documents.data.remote.FilmApi
import com.example.cinema.documents.presentation.screen.home.HomeScreen
import com.example.cinema.documents.presentation.screen.moviedetail.MovieDetailScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController
            )
        }
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