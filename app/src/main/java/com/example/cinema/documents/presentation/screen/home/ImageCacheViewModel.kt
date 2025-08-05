package com.example.cinema.documents.presentation.screen.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImageCacheViewModel : ViewModel() {
    private val _loadedUrls = MutableStateFlow<Set<String>>(emptySet())
    val loadedUrls = _loadedUrls.asStateFlow()

    fun markUrlAsLoaded(url: String) {
        _loadedUrls.value = _loadedUrls.value + url
    }

    fun isUrlLoaded(url: String): Boolean {
        return _loadedUrls.value.contains(url)
    }
}