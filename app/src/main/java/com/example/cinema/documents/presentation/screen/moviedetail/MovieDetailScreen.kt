package com.example.cinema.documents.presentation.screen.moviedetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cinema.R
import com.example.cinema.documents.domain.domain.Trailer
import com.example.cinema.documents.presentation.viewmodel.FilmListViewModel
import com.example.cinema.documents.presentation.viewmodel.StaffViewModel

@Composable
fun MovieDetailScreen(
    filmId: Int,
    viewModel: FilmListViewModel = hiltViewModel(),
    staffViewModel: StaffViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val film by viewModel.film.collectAsState()
    val trailer by viewModel.trailer.collectAsState()
    var isClicked by remember { mutableStateOf(false) }

    val seasonsState by viewModel.seasonsState.collectAsState()
    val api = "f503c2e2-dcac-4045-9748-ddd23fc0bafe"
    LaunchedEffect(Unit) {
        viewModel.loadSeasonsAndEpisodes(filmId, api)
    }
    LaunchedEffect(filmId) {
        viewModel.loadFilm(filmId, api)
    }
    LaunchedEffect(filmId) {
        staffViewModel.loadActors(filmId, api)
        viewModel.getTrailer(filmId)
    }


    film?.let { movie ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Box {
                    val embedUrl = "https://www.youtube.com/embed/5iw-hJ6xteE?si=T58BKBhotMqGKnnl"

                    YoutubeWebViewPlayer(embedUrl = embedUrl)
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(vertical = 32.dp, horizontal = 26.dp)
                            .size(32.dp)
                            .clickable(
                                onClick = onBackClick,
                                indication = ripple(false),
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        tint = Color.Black
                    )
                    Column(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = movie.logoUrl,
                            contentDescription = movie.nameRu,
                            modifier = Modifier
                                .width(180.dp)
                        )
                        Text(
                            text = "${movie.ratingKinopoisk ?: 0.0} ${movie.nameRu ?: ""}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFF_B5B5C9)
                        )
                        Text(
                            text = "${movie.year}, ${
                                movie.genres.take(2).joinToString { it.genre }
                            }",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFF_B5B5C9)
                        )
                        Text(
                            text = "${movie.countries.joinToString { it.country }}, ${movie.filmLength} мин, ${
                                movie.ratingAgeLimits?.replace(
                                    "age",
                                    ""
                                )?.plus("+") ?: ""
                            }",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFF_B5B5C9)
                        )
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ImageBox(R.drawable.ic_heart) {}
                            ImageBox(R.drawable.ic_bookmark) {}
                            ImageBox(R.drawable.ic_eye_close) {}
                            ImageBox(R.drawable.ic_share) {}
                            ImageBox(R.drawable.ic_settings) {}

                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = movie.shortDescription.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    lineHeight = 22.sp,
                    color = Color(0xFF_272727),
                    modifier = Modifier.padding(horizontal = 26.dp)
                )
                Text(
                    text = movie.description.toString(),
                    modifier = Modifier
                        .padding(26.dp)
                        .clickable(
                            onClick = {
                                isClicked = !isClicked
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isClicked) movie.description?.length ?: 5 else 5,
                )

                if (movie.type.toString() == "FILM") {
                    null
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 26.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Сезоны и серии",
                            color = Color(0xFF_272727),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Все",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFF_3D3BFF)
                        )
                    }
                    when (seasonsState) {
                        is SeasonsUiState.Loading -> Text("Загрузка сезонов...")
                        is SeasonsUiState.Success -> {
                            val data = seasonsState as SeasonsUiState.Success
                            Text(
                                text = "${data.seasonsCount} сезон, ${data.episodesCount} серий",
                                fontWeight = FontWeight.W400,
                                fontSize = 12.sp,
                                color = Color(0xFF_838391),
                                modifier = Modifier.padding(start = 26.dp)
                            )
                        }

                        is SeasonsUiState.Error -> Text("Ошибка загрузки: ${(seasonsState as SeasonsUiState.Error).message}")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                ActorsScrollableGrid()
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun ImageBox(
    image: Int,
    onCLick: () -> Unit = {}
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = "..",
        modifier = Modifier
            .size(44.dp)
            .clickable(
                onClick = onCLick,
                indication = ripple(false, 20.dp, Color.Gray),
                interactionSource = remember { MutableInteractionSource() }
            )
    )
}

@Composable
fun ActorsScrollableGrid(
    viewModel: StaffViewModel = hiltViewModel()
) {
    val actors by viewModel.actors.collectAsState()
    val actorChunks = actors.chunked(4)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "В фильме снимались",
            fontSize = 18.sp,
            fontWeight = FontWeight.W600,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = actorChunks.size.toString(),
                color = Color(0xFF_3D3BFF)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                tint = Color(0xFF_3D3BFF),
                modifier = Modifier.size(18.dp)
            )
        }
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(start = 26.dp, end = 0.dp)
    ) {
        items(actorChunks) { chunk ->
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Spacer(modifier = Modifier.height(33.dp))
                chunk.forEach { actor ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            model = actor.posterUrl,
                            contentDescription = actor.nameRu,
                            modifier = Modifier
                                .width(49.dp)
                                .height(68.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier.width(150.dp)
                        ) {
                            Text(
                                text = actor.nameRu ?: actor.nameEn ?: "",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = actor.description ?: "",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}


