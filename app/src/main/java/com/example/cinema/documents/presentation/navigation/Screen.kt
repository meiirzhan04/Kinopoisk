package com.example.cinema.documents.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Account : Screen("account")
    object Detail : Screen("detail/{filmId}") {
        fun createRoute(filmId: Int) = "detail/$filmId"
    }
}
