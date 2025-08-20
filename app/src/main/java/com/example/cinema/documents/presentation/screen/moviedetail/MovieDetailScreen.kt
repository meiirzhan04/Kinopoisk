@file:Suppress("DEPRECATION")

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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cinema.R
import com.example.cinema.documents.domain.model.FilmDetail
import com.example.cinema.documents.domain.model.Staff
import com.example.cinema.documents.presentation.components.movie.MovieCard
import com.example.cinema.documents.presentation.navigation.Screen
import com.example.cinema.documents.presentation.viewmodel.FilmListViewModel
import com.example.cinema.documents.presentation.viewmodel.StaffViewModel

@Composable
fun MovieDetailScreen(
    filmId: Int,
    viewModel: FilmListViewModel = hiltViewModel(),
    staffViewModel: StaffViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    navController: NavController
) {
    val film by viewModel.film.collectAsState()
    var isClicked by remember { mutableStateOf(false) }

    val seasonsState by viewModel.seasonsState.collectAsState()
    val staffList by staffViewModel.actors.collectAsState()
    val images by staffViewModel.images.collectAsState()
    val similarFilms by staffViewModel.similarFilms.collectAsState()

    val api = "d6ec8969-b72b-4692-95c0-2968f5fb760d"

    val actors = staffList.filter { it.professionKey == "ACTOR" }
    val creators = staffList.filter { it.professionKey != "ACTOR" }

    LaunchedEffect(filmId) {
        viewModel.loadFilm(filmId, api)
        viewModel.loadSeasonsAndEpisodes(filmId, api)
        viewModel.getTrailer(filmId)
        staffViewModel.loadActors(filmId, api)
        staffViewModel.loadImages(filmId, api)
        staffViewModel.loadSimilarFilms(filmId, api)
    }

    film?.let { movie ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Box {
                    YoutubeWebViewPlayer(
                        embedUrl = "https://www.youtube.com/embed/5iw-hJ6xteE?si=T58BKBhotMqGKnnl"
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(32.dp)
                            .size(32.dp)
                            .clickable(
                                onClick = onBackClick,
                                indication = ripple(false),
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        tint = Color.Black
                    )
                    MovieInfo(movie, Modifier.align(Alignment.BottomCenter))
                }
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = movie.shortDescription.orEmpty(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    lineHeight = 22.sp,
                    color = Color(0xFF272727),
                    modifier = Modifier.padding(horizontal = 26.dp)
                )
                Text(
                    text = movie.description.orEmpty(),
                    modifier = Modifier
                        .padding(26.dp)
                        .clickable(
                            onClick = { isClicked = !isClicked },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isClicked) Int.MAX_VALUE else 5,
                )
                if (movie.type != "FILM") {
                    SectionHeader("Сезоны и серии", trailing = {
                        Text(
                            "Все",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFF3D3BFF)
                        )
                    })
                    when (seasonsState) {
                        is SeasonsUiState.Loading -> Text("Загрузка сезонов...")
                        is SeasonsUiState.Success -> {
                            val data = seasonsState as SeasonsUiState.Success
                            Text(
                                text = "${data.seasonsCount} сезон, ${data.episodesCount} серий",
                                fontSize = 12.sp,
                                color = Color(0xFF838391),
                                modifier = Modifier.padding(start = 26.dp)
                            )
                        }

                        is SeasonsUiState.Error -> Text("Ошибка загрузки")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                PeopleGrid("В фильме снимались", actors)
                Spacer(modifier = Modifier.height(32.dp))
            }
            item { PeopleGrid("Над фильмом работали", creators)
                Spacer(modifier = Modifier.height(32.dp)) }
            item {
                SectionHeader("Галерея", count = images.size)
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 26.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(images) { image ->
                        AsyncImage(
                            model = image.previewUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .height(108.dp)
                                .width(192.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                SectionHeader("Похожие фильмы", count = similarFilms.size)
                Spacer(modifier = Modifier.height(24.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 26.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(similarFilms) { similar ->
                        MovieCard(
                            title = similar.nameRu ?: similar.nameEn.orEmpty(),
                            genre = movie.genres.firstOrNull()?.genre ?: "Жанр не указан",
                            rating = movie.ratingKinopoisk ?: 0.0,
                            posterUrl = similar.posterUrlPreview.orEmpty(),
                            onClick = {
                                navController.navigate(
                                    Screen.Detail.createRoute(
                                        similar.filmId ?: 0
                                    )
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, count: Int? = null, trailing: @Composable (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.W600)
        trailing ?: run {
            count?.let {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(it.toString(), color = Color(0xFF3D3BFF))
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF3D3BFF)
                    )
                }
            }
        }
    }
}

@Composable
fun PeopleGrid(title: String, people: List<Staff>) {
    SectionHeader(title, count = people.size)
    val chunks = people.chunked(4)
    LazyRow(
        contentPadding = PaddingValues(start = 26.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(chunks) { chunk ->
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Spacer(modifier = Modifier.height(33.dp))
                chunk.forEach { person ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = person.posterUrl,
                            contentDescription = person.nameRu,
                            modifier = Modifier
                                .width(49.dp)
                                .height(68.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Column(Modifier.width(150.dp)) {
                            Text(
                                person.nameRu ?: person.nameEn.orEmpty(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                person.description.orEmpty(),
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

@Composable
fun MovieInfo(movie: FilmDetail, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = movie.logoUrl,
            contentDescription = movie.nameRu,
            modifier = Modifier.width(180.dp)
        )
        Text(
            text = "${movie.ratingKinopoisk ?: 0.0} ${movie.nameRu ?: ""}",
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            color = Color(0xFFB5B5C9)
        )
        Text(
            text = "${movie.year}, ${movie.genres.take(2).joinToString { it.genre }}",
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            color = Color(0xFFB5B5C9)
        )
        Text(
            text = "${movie.countries.joinToString { it.country }}, ${movie.filmLength} мин, " +
                    (movie.ratingAgeLimits?.replace("age", "")?.plus("+") ?: ""),
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            color = Color(0xFFB5B5C9)
        )

        Row(
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

@Composable
fun ImageBox(
    image: Int,
    onClick: () -> Unit = {}
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        modifier = Modifier
            .size(44.dp)
            .clickable(
                onClick = onClick,
                indication = ripple(false, 20.dp, Color.Gray),
                interactionSource = remember { MutableInteractionSource() }
            )
    )
}
