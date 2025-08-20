package com.example.cinema.documents.presentation.screen.moviedetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import androidx.core.net.toUri

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YoutubeWebViewPlayer(embedUrl: String) {
    AndroidView(
        factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.domStorageEnabled = true
                webChromeClient = WebChromeClient()
                loadUrl(embedUrl)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    )
}


fun convertToEmbedUrl(originalUrl: String): String {
    val videoId = originalUrl.substringAfter("v=")
    return "https://www.youtube.com/embed/$videoId"
}

