package com.fred.demomoviedb.view.fragment

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
import com.fred.demomoviedb.R
import com.fred.demomoviedb.databinding.FragmentTvShowsBinding
import com.fred.demomoviedb.model.DataSource
import com.fred.demomoviedb.model.Movie
import com.fred.demomoviedb.usecases.MoviesRepository
import com.fred.demomoviedb.utils.add
import com.fred.demomoviedb.utils.setVisible
import com.fred.demomoviedb.view.adapter.MoviesAdapter
import com.fred.demomoviedb.viewModel.MovieViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

class TvShowsFragment : Fragment(), MoviesAdapter.MovieActions {

    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel: MovieViewModel by activityViewModels()

    private lateinit var mPopularShowsAdapter: MoviesAdapter
    private lateinit var popularShowsLayout: LinearLayoutManager
    private var popularShowPage: Int = 1

    private lateinit var mRatedShowsAdapter: MoviesAdapter
    private lateinit var ratedShowsLayout: LinearLayoutManager
    private var ratedShowPage: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowsBinding.inflate(inflater, container, false)
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
            DetailsFragment.newInstance(DataSource.SHOW),
            DetailsFragment.NAME
        )
    }

    private fun initComponents() {
        popularShowsLayout = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        ratedShowsLayout = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.apply {
            recyclerPopularShows.layoutManager = popularShowsLayout
            mPopularShowsAdapter = MoviesAdapter(mutableListOf(), this@TvShowsFragment)
            recyclerPopularShows.adapter = mPopularShowsAdapter

            recyclerRatedShows.layoutManager = ratedShowsLayout
            mRatedShowsAdapter = MoviesAdapter(mutableListOf(), this@TvShowsFragment)
            recyclerRatedShows.adapter = mRatedShowsAdapter

            reconnectingShow.setOnClickListener {
                it.setVisible(false)
                shimmerShows.showShimmer()
                imageViewShowsConnectionStatus.setVisible(false)
                getPopularShows()
                getRatedShows()
            }
        }
        getPopularShows()
        getRatedShows()
    }

    private fun getPopularShows() {
        movieViewModel.viewModelScope.launch {
            MoviesRepository.getPopularShows(
                popularShowPage,
                onSuccess = ::onPopularShowsFetched,
                onError = ::onError
            )
        }
    }

    private fun getRatedShows() {
        movieViewModel.viewModelScope.launch {
            MoviesRepository.getRatedShows(
                ratedShowPage,
                onSuccess = ::onTopRatedShowsFetched,
                onError = ::onError
            )
        }
    }

    private fun onPopularShowsFetched(movies: List<Movie>) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.apply {
                shimmerShows.hideShimmer()
                mainContainerShowsList.setVisible(true)
            }
            mPopularShowsAdapter.appendMovies(movies)
            attachPopularShowsOnScrollListener()
        }, 1500)
    }

    private fun onTopRatedShowsFetched(movies: List<Movie>) {
        mRatedShowsAdapter.appendMovies(movies)
        attachTopRatedShowsOnScrollListener()
    }

    private fun attachPopularShowsOnScrollListener() {
        binding.recyclerPopularShows.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularShowsLayout.itemCount
                val visibleItemCount = popularShowsLayout.childCount
                val firstVisibleItem = popularShowsLayout.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.recyclerPopularShows.removeOnScrollListener(this)
                    popularShowPage++
                    getPopularShows()
                }
            }
        })
    }

    private fun attachTopRatedShowsOnScrollListener() {
        binding.recyclerRatedShows.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = ratedShowsLayout.itemCount
                val visibleItemCount = ratedShowsLayout.childCount
                val firstVisibleItem = ratedShowsLayout.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.recyclerRatedShows.removeOnScrollListener(this)
                    ratedShowPage++
                    getRatedShows()
                }
            }
        })
    }

    private fun onError() {
        Handler(Looper.getMainLooper()).postDelayed({
            Toasty.info(
                requireContext(),
                getString(R.string.no_internet_connection),
                Toasty.LENGTH_SHORT,
                true
            ).show()
            binding.apply {
                shimmerShows.hideShimmer()
                imageViewShowsConnectionStatus.setVisible(true)
                reconnectingShow.setVisible(true)
                mainContainerShowsList.setVisible(false)
            }
        }, 2000)
    }

    companion object {
        const val NAME = "TvShows"
        fun newInstance() = TvShowsFragment()
    }
}