package com.example.cinema.documents.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cinema.R
import com.example.cinema.documents.presentation.components.MovieListOrShimmer
import com.example.cinema.documents.presentation.navigation.Screen
import com.example.cinema.documents.presentation.viewmodel.FilmListUiState
import com.example.cinema.documents.presentation.viewmodel.FilmListViewModel

@Composable
fun HomeScreen(
    viewModel: FilmListViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFilms(
            listOf(
                263531, 1309166, 1023, 302, 303, 1002,
                304, 305, 306, 307, 308, 309,
                310, 311, 312, 313, 314
            ),
            "d6ec8969-b72b-4692-95c0-2968f5fb760d"
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is FilmListUiState.Loading -> {
                item {
                    Column(modifier = Modifier.padding(top = 64.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_text),
                            contentDescription = "",
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        RowText(text = "Премьера", onClick = {})
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    MovieListOrShimmer(emptyList()) {}
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    RowText(text = "Популярное", onClick = {})
                    Spacer(modifier = Modifier.height(16.dp))
                    MovieListOrShimmer(emptyList()) {}
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    RowText(text = "Боевики США", onClick = {})
                    Spacer(modifier = Modifier.height(16.dp))
                    MovieListOrShimmer(emptyList()) {}
                }
            }

            is FilmListUiState.Success -> {
                val films = state.films

                item {
                    Column(modifier = Modifier.padding(top = 32.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_text),
                            contentDescription = "",
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        RowText(text = "Премьера", onClick = {})
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    MovieListOrShimmer(films) { film ->
                        navController.navigate(Screen.Detail.createRoute(film.kinopoiskId))
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    RowText(text = "Популярное", onClick = {})
                    Spacer(modifier = Modifier.height(16.dp))
                    MovieListOrShimmer(films) { film ->
                        navController.navigate(Screen.Detail.createRoute(film.kinopoiskId))
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    RowText(text = "Боевики США", onClick = {})
                    Spacer(modifier = Modifier.height(16.dp))
                    MovieListOrShimmer(films) { film ->
                        navController.navigate(Screen.Detail.createRoute(film.kinopoiskId))
                    }
                }
            }

            is FilmListUiState.Error -> {
                item {
                    Text(
                        text = "Ошибка загрузки: ${state.message}",
                        modifier = Modifier.padding(24.dp),
                        color = Color.Red
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



@Composable
fun RowText(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color(0xFF_272727),
            fontWeight = FontWeight.W600,
        )
        Text(
            text = "Все",
            fontSize = 14.sp,
            color = Color(0xFF_3D3BFF),
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .clickable(
                    onClick = onClick,
                    indication = ripple(false),
                    interactionSource = remember { MutableInteractionSource() }
                )
        )
    }
}
