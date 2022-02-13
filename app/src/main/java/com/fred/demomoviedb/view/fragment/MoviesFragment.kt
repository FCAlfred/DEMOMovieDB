package com.fred.demomoviedb.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fred.demomoviedb.R
import com.fred.demomoviedb.databinding.FragmentMoviesBinding
import com.fred.demomoviedb.model.Movie
import com.fred.demomoviedb.usecases.MoviesRepository
import com.fred.demomoviedb.utils.add
import com.fred.demomoviedb.utils.setVisible
import com.fred.demomoviedb.view.adapter.MoviesAdapter
import com.fred.demomoviedb.viewModel.MovieViewModel
import es.dmoral.toasty.Toasty

class MoviesFragment : Fragment(), MoviesAdapter.MovieActions {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel: MovieViewModel by activityViewModels()

    private lateinit var mPopularMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesLayout: LinearLayoutManager
    private var popularMoviePage: Int = 1

    private lateinit var mRatedMoviesAdapter: MoviesAdapter
    private lateinit var ratedMoviesLayout: LinearLayoutManager
    private var ratedMoviePage: Int = 1

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
            DetailsFragment.newInstance(),
            DetailsFragment.NAME
        )
    }

    private fun initComponents() {
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
            mRatedMoviesAdapter = MoviesAdapter(mutableListOf(), this@MoviesFragment)
            recyclerRated.adapter = mRatedMoviesAdapter
        }

        getPopularMovies()
        getRatedMovies()
    }

    private fun getPopularMovies() {
        MoviesRepository.getPopularMovies(
            popularMoviePage,
            onSuccess = ::onPopularMoviesFetched,
            onError = ::onError
        )
    }

    private fun getRatedMovies() {
        MoviesRepository.getTopRatedMovies(
            ratedMoviePage,
            onSuccess = ::onTopRatedMoviesFetched,
            onError = ::onError
        )
    }

    private fun onPopularMoviesFetched(movies: List<Movie>) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.apply {
                shimmer.hideShimmer()
                mainContainerList.setVisible(true)
            }
            mPopularMoviesAdapter.appendMovies(movies)
            attachPopularMoviesOnScrollListener()
        }, 1500)
    }

    private fun onTopRatedMoviesFetched(movies: List<Movie>) {
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
        Handler(Looper.getMainLooper()).postDelayed({
            Toasty.error(
                requireContext(),
                getString(R.string.no_internet_connection),
                Toasty.LENGTH_SHORT,
                true
            )
                .show()
            binding.apply {
                shimmer.hideShimmer()
                imageViewConnectionStatus.setVisible(true)
                mainContainerList.setVisible(false)
            }
        }, 2000)
    }

    companion object {
        const val NAME = "Movies"
        fun newInstance() = MoviesFragment()
    }
}