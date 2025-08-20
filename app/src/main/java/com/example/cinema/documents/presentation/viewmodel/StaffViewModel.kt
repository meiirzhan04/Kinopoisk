package com.example.cinema.documents.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.documents.data.remote.FilmApi
import com.example.cinema.documents.domain.model.ImageItem
import com.example.cinema.documents.domain.model.SimilarFilm
import com.example.cinema.documents.domain.model.SimilarFilmList
import com.example.cinema.documents.domain.model.Staff
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffViewModel @Inject constructor(
    private val api: FilmApi
) : ViewModel() {

    private val _actors = MutableStateFlow<List<Staff>>(emptyList())
    val actors: StateFlow<List<Staff>> = _actors

    private val _images = MutableStateFlow<List<ImageItem>>(emptyList())
    val images: StateFlow<List<ImageItem>> = _images

    private val _similarFilms = MutableStateFlow<List<SimilarFilmList>>(emptyList())
    val similarFilms: StateFlow<List<SimilarFilmList>> = _similarFilms
    fun loadActors(filmId: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                val result = api.getActors(filmId, apiKey)
                _actors.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                _actors.value = emptyList()
            }
        }
    }


    fun loadImages(filmId: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                val res = api.getImages(filmId, apiKey, type = "SHOOTING", page = 1)
                if (res.isSuccessful) {
                    _images.value = res.body()?.items.orEmpty()
                } else {
                    _images.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _images.value = emptyList()
            }
        }
    }

    fun loadSimilarFilms(filmId: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = api.getSimilarFilms(filmId, apiKey)
                _similarFilms.value = response.items
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}