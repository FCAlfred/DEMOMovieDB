package com.fred.demomoviedb.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fred.demomoviedb.model.Movie
import com.fred.demomoviedb.model.RatedMovie
import com.fred.demomoviedb.model.RatedShow
import com.fred.demomoviedb.model.Show

class MovieViewModel : ViewModel() {

    private var _currentMovieSelected = MutableLiveData<Movie>()
    val currentMovieSelected: LiveData<Movie> get() = _currentMovieSelected

    fun setMovieSelected(set: Movie) {
        _currentMovieSelected.value = set
    }

    private var _currentRatedMovieSelected = MutableLiveData<RatedMovie>()
    val currentRatedMovieSelected: LiveData<RatedMovie> get() = _currentRatedMovieSelected

    fun setRatedMovieSelected(set: RatedMovie) {
        _currentRatedMovieSelected.value = set
    }

    private var _currentShowSelected = MutableLiveData<Show>()
    val currentShowSelected: LiveData<Show> get() = _currentShowSelected

    fun setShowSelected(set: Show) {
        _currentShowSelected.value = set
    }

    private var _currentRatedShowSelected = MutableLiveData<RatedShow>()
    val currentRatedShowSelected: LiveData<RatedShow> get() = _currentRatedShowSelected

    fun setRatedShowSelected(set: RatedShow) {
        _currentRatedShowSelected.value = set
    }
}