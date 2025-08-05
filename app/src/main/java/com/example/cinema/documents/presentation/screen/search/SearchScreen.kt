@file:Suppress("DEPRECATION")

package com.example.cinema.documents.presentation.screen.search

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.cinema.R
import com.example.cinema.documents.presentation.navigation.Screen
import com.example.cinema.documents.presentation.screen.home.ImageCacheViewModel
import com.example.cinema.documents.presentation.screen.search.SearchViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer


@SuppressLint("DefaultLocale")
@Composable
fun SearchScreen(
    navController: NavController,
    placeholder: String = "Фильмы, актёры, режиссёры",
    onFilterClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    showFilter: Boolean = true,
    viewModel: SearchViewModel = hiltViewModel(),
    imageCacheViewModel: ImageCacheViewModel = hiltViewModel()
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    var query by rememberSaveable { mutableStateOf("") }
    val results by viewModel.results.collectAsState()

    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 32.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF_B5B5C9).copy(alpha = 0.4f),
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = if (isFocused || query.isNotEmpty()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color(0xFF_838390)
                    },
                    modifier = Modifier.size(20.dp)
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            viewModel.searchFilms(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                            },
                        textStyle = TextStyle(
                            color = Color(0xFF_272727),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.searchFilms(query)
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        interactionSource = interactionSource,
                    )
                    if (query.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                color = Color(0xFF_838390),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }

                AnimatedVisibility(
                    visible = query.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                if (showFilter && onFilterClick != null) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    )
                }
                if (showFilter && onFilterClick != null) {
                    IconButton(
                        onClick = onFilterClick,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = "Filter",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
        LazyColumn {
            items(results) { film ->
                val alreadyLoaded =
                    imageCacheViewModel.isUrlLoaded(film.posterUrlPreview.toString())
                var isLoading by remember { mutableStateOf(!alreadyLoaded) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(156.dp)
                            .width(111.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                navController.navigate(Screen.Detail.createRoute(film.filmId))
                            }
                    ) {
                        AsyncImage(
                            model = film.posterUrlPreview,
                            contentDescription = "Poster",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .placeholder(
                                    visible = isLoading,
                                    color = Color.LightGray.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(4.dp),
                                    highlight = PlaceholderHighlight.shimmer()
                                ),
                            onState = { state ->
                                if (state !is AsyncImagePainter.State.Loading && isLoading) {
                                    isLoading = false
                                    imageCacheViewModel.markUrlAsLoaded(film.posterUrlPreview.toString())
                                }
                            }
                        )

                        if (!isLoading) {
                            val ratingValue = film.rating?.toFloatOrNull() ?: 0f
                            Box(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF3D5AFE))
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = String.format("%.1f", ratingValue),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Column {
                        Text(
                            text = film.nameRu ?: "",
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF272727),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.placeholder(
                                visible = isLoading,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = film.year.toString(),
                            fontSize = 12.sp,
                            color = Color(0xFF838390),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.placeholder(
                                visible = isLoading,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                        )
                    }

                }
            }
        }
    }
}