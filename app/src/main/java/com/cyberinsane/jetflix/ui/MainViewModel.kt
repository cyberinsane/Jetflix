package com.cyberinsane.jetflix.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberinsane.jetflix.domain.model.MovieCollection
import com.cyberinsane.jetflix.domain.model.TVCollection
import com.cyberinsane.jetflix.domain.model.ext.onError
import com.cyberinsane.jetflix.domain.model.ext.onSuccess
import com.cyberinsane.jetflix.domain.model.ext.zip
import com.cyberinsane.jetflix.domain.usecase.GetMovieCollectionUseCase
import com.cyberinsane.jetflix.domain.usecase.GetTVCollectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val getTVCollectionUseCase: GetTVCollectionUseCase,
    val getMovieCollectionUseCase: GetMovieCollectionUseCase,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(MainViewState())
    private val refreshing = MutableStateFlow(false)

    val state: StateFlow<MainViewState>
        get() = _state

    init {
        getShows()
    }

    fun getShows() {
        viewModelScope.launch {
            refreshing.value = true
            getMovieCollectionUseCase(Unit)
                .zip(getTVCollectionUseCase(Unit))
                .onSuccess {
                    println(it.second)
                    println(it.first)
                    _state.value = MainViewState(it.second, it.first)
                }.onError {
                    println(it)
                }
            refreshing.value = false
        }
    }
}

data class MainViewState(
    val tvCollection: TVCollection? = null,
    var movieCollection: MovieCollection? = null,
    val refreshing: Boolean = false
)