package com.example.cinema.documents.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.documents.data.remote.FilmApi
import com.example.cinema.documents.domain.domain.Staff
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
}