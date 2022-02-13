package com.fred.demomoviedb.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fred.demomoviedb.model.Movie

class MovieViewModel() : ViewModel() {
    private var _currentMovieSelected = MutableLiveData<Movie>()
    val currentMovieSelected: LiveData<Movie> get() = _currentMovieSelected

    fun setMovieSelected(set: Movie) {
        _currentMovieSelected.value = set
    }
}