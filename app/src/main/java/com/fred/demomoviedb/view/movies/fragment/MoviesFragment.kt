package com.fred.demomoviedb.view.movies.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.fred.demomoviedb.R
import com.fred.demomoviedb.components.Movies
import com.fred.demomoviedb.databinding.FragmentMoviesBinding
import com.fred.demomoviedb.model.Movie
import com.fred.demomoviedb.model.RatedMovie
import com.fred.demomoviedb.usecases.local.MoviesDb
import com.fred.demomoviedb.usecases.network.MoviesRepository
import com.fred.demomoviedb.utils.add
import com.fred.demomoviedb.utils.setVisible
import com.fred.demomoviedb.view.movies.adapter.MoviesAdapter
import com.fred.demomoviedb.view.movies.adapter.MoviesRatedAdapter
import com.fred.demomoviedb.viewModel.MovieViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

class MoviesFragment : Fragment(), MoviesAdapter.MovieActions, MoviesRatedAdapter.MovieActions {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel: MovieViewModel by activityViewModels()

    private lateinit var mPopularMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesLayout: LinearLayoutManager
    private var popularMoviePage: Int = 1

    private lateinit var mRatedMoviesAdapter: MoviesRatedAdapter
    private lateinit var ratedMoviesLayout: LinearLayoutManager
    private var ratedMoviePage: Int = 1

    private lateinit var localDb: MoviesDb

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    override fun onClickedMovie(selectedMovie: Movie) {
        movieViewModel.setMovieSelected(selectedMovie)
        requireActivity().supportFragmentManager.add(
            R.id.main_container,
            DetailsFragment.newInstance(Movies.POPULAR),
            DetailsFragment.NAME
        )
    }

    override fun onClickedRatedMovie(selectedMovie: RatedMovie) {
        movieViewModel.setRatedMovieSelected(selectedMovie)
        requireActivity().supportFragmentManager.add(
            R.id.main_container,
            DetailsFragment.newInstance(Movies.RATED),
            DetailsFragment.NAME
        )
    }

    private fun initComponents() {
        localDb = Room.databaseBuilder(
            requireContext(),
            MoviesDb::class.java,
            "PopularMovies"
        )
            .build()
        popularMoviesLayout = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        ratedMoviesLayout = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.apply {
            recyclerPopular.layoutManager = popularMoviesLayout
            mPopularMoviesAdapter = MoviesAdapter(mutableListOf(), this@MoviesFragment)
            recyclerPopular.adapter = mPopularMoviesAdapter

            recyclerRated.layoutManager = ratedMoviesLayout
            mRatedMoviesAdapter = MoviesRatedAdapter(mutableListOf(), this@MoviesFragment)
            recyclerRated.adapter = mRatedMoviesAdapter

            reconnecting.setOnClickListener {
                it.setVisible(false)
                shimmer.showShimmer()
                imageViewConnectionStatus.setVisible(false)
                getPopularMovies()
                getRatedMovies()
            }
        }
        getPopularMovies()
        getRatedMovies()
    }

    private fun getPopularMovies() {
        movieViewModel.viewModelScope.launch {
            MoviesRepository.getPopularMovies(
                popularMoviePage,
                onSuccess = ::onPopularMoviesFetched,
                onError = ::onError
            )
        }
    }

    private fun getRatedMovies() {
        movieViewModel.viewModelScope.launch {
            MoviesRepository.getTopRatedMovies(
                ratedMoviePage,
                onSuccess = ::onTopRatedMoviesFetched,
                onError = ::onError
            )
        }
    }

    private fun onPopularMoviesFetched(movies: List<Movie>) {
        movieViewModel.viewModelScope.launch {
            if (localDb.movieDao().getAllMovies().isNullOrEmpty()) {
                localDb.movieDao().insertMovieList(movies)
            } else {
                localDb.movieDao().updateMovieList(movies)
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            binding.apply {
                shimmer.hideShimmer()
                mainContainerList.setVisible(true)
            }
            mPopularMoviesAdapter.appendMovies(movies)
            attachPopularMoviesOnScrollListener()
        }, 1500)
    }

    private fun onTopRatedMoviesFetched(movies: List<RatedMovie>) {
        movieViewModel.viewModelScope.launch {
            if (localDb.ratedMovieDao().getAllRatedMovies().isNullOrEmpty()) {
                localDb.ratedMovieDao().insertRatedMovieList(movies)
            } else {
                localDb.ratedMovieDao().updateRatedMovieList(movies)
            }
        }
        mRatedMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }

    private fun attachPopularMoviesOnScrollListener() {
        binding.recyclerPopular.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayout.itemCount
                val visibleItemCount = popularMoviesLayout.childCount
                val firstVisibleItem = popularMoviesLayout.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.recyclerPopular.removeOnScrollListener(this)
                    popularMoviePage++
                    getPopularMovies()
                }
            }
        })
    }

    private fun attachTopRatedMoviesOnScrollListener() {
        binding.recyclerRated.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = ratedMoviesLayout.itemCount
                val visibleItemCount = ratedMoviesLayout.childCount
                val firstVisibleItem = ratedMoviesLayout.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.recyclerRated.removeOnScrollListener(this)
                    ratedMoviePage++
                    getRatedMovies()
                }
            }
        })
    }

    private fun onError() {
        validateLocalDb()
    }

    private fun validateLocalDb() {
        movieViewModel.viewModelScope.launch {
            val localMovieList = localDb.movieDao().getAllMovies()
            val localRatedMoviesList = localDb.ratedMovieDao().getAllRatedMovies()
            Handler(Looper.getMainLooper()).postDelayed({
                if (localMovieList.isNullOrEmpty()) {
                    Toasty.info(
                        requireContext(),
                        getString(R.string.no_internet_connection),
                        Toasty.LENGTH_SHORT,
                        true
                    ).show()
                    binding.apply {
                        shimmer.hideShimmer()
                        imageViewConnectionStatus.setVisible(true)
                        reconnecting.setVisible(true)
                        mainContainerList.setVisible(false)
                    }
                } else {
                    binding.apply {
                        shimmer.hideShimmer()
                        mainContainerList.setVisible(true)
                    }
                    mPopularMoviesAdapter.appendMovies(localMovieList)
                    mRatedMoviesAdapter.appendMovies(localRatedMoviesList)
                    attachPopularMoviesOnScrollListener()
                    Toasty.info(
                        requireContext(),
                        getString(R.string.offline_mode),
                        Toasty.LENGTH_SHORT,
                        true
                    ).show()
                }
            }, 2000)
        }
    }

    companion object {
        const val NAME = "movies"
        fun newInstance() = MoviesFragment()
    }
}