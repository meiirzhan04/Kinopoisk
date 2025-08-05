@file:Suppress("DEPRECATION")

package com.example.cinema.documents.presentation.components.movie

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.cinema.documents.presentation.screen.home.ImageCacheViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer

@SuppressLint("DefaultLocale")
@Composable
fun MovieCard(
    title: String,
    genre: String,
    rating: Double,
    posterUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    imageCacheViewModel: ImageCacheViewModel = hiltViewModel()
) {
    val alreadyLoaded = imageCacheViewModel.isUrlLoaded(posterUrl)
    var isLoading by remember { mutableStateOf(!alreadyLoaded) }

    Column(
        modifier = modifier
            .width(111.dp)
    ) {
        Box(
            modifier = Modifier
                .height(156.dp)
                .width(111.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable { onClick() }
        ) {
            AsyncImage(
                model = posterUrl,
                contentDescription = "$title Poster",
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
                        imageCacheViewModel.markUrlAsLoaded(posterUrl)
                    }
                }
            )

            if (!isLoading) {
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF3D5AFE))
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%.1f", rating),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
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
            text = genre,
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