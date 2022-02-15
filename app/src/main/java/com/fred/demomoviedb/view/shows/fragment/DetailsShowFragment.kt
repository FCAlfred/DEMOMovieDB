package com.fred.demomoviedb.view.shows.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fred.demomoviedb.R
import com.fred.demomoviedb.components.TvShows
import com.fred.demomoviedb.databinding.FragmentDetailsShowBinding
import com.fred.demomoviedb.model.RatedShow
import com.fred.demomoviedb.model.Show
import com.fred.demomoviedb.usecases.network.MoviesRepository
import com.fred.demomoviedb.utils.setGoneVisibility
import com.fred.demomoviedb.utils.setImage
import com.fred.demomoviedb.utils.setVisible
import com.fred.demomoviedb.view.shows.adapter.ShowsAdapter
import com.fred.demomoviedb.view.shows.adapter.ShowsRatedAdapter
import com.fred.demomoviedb.viewModel.MovieViewModel
import kotlinx.coroutines.launch

class DetailsShowFragment(private val showType: TvShows) : Fragment(), ShowsAdapter.ShowsActions,
    ShowsRatedAdapter.ShowsActions {

    private var _binding: FragmentDetailsShowBinding? = null
    private val binding get() = _binding!!
    private val showViewModel: MovieViewModel by activityViewModels()
    private lateinit var mRecommendationShowsAdapter: ShowsAdapter
    private lateinit var mRecommendationRatedShowsAdapter: ShowsRatedAdapter
    private lateinit var recommendedShowsLayout: LinearLayoutManager
    private lateinit var recommendedRatedShowsLayout: LinearLayoutManager
    private var recommendedPage: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        setObservers()
    }

    override fun onClickedShow(selectedShow: Show) {
        showViewModel.setShowSelected(selectedShow)
    }

    override fun onClickedRatedShow(selectedShow: RatedShow) {
        showViewModel.setRatedShowSelected(selectedShow)
    }

    private fun initComponents() {
        var isRecyclerVisible = false
        mRecommendationShowsAdapter = ShowsAdapter(mutableListOf(), this@DetailsShowFragment)
        mRecommendationRatedShowsAdapter =
            ShowsRatedAdapter(mutableListOf(), this@DetailsShowFragment)
        recommendedShowsLayout = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recommendedRatedShowsLayout = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.button.apply {
            setOnClickListener {
                isRecyclerVisible = !isRecyclerVisible
                text = if (isRecyclerVisible) {
                    getString(R.string.hide_recommendations)
                } else {
                    getString(R.string.show_recommendations)
                }
                binding.recyclerRecommendation.setGoneVisibility(!isRecyclerVisible)
                binding.textViewMovieDescription.setVisible(!isRecyclerVisible)
            }
        }
    }

    private fun setObservers() {
        if (showType.name == TvShows.POPULAR.name) {
            showViewModel.currentShowSelected.observe(viewLifecycleOwner) {
                binding.apply {
                    setImage(
                        requireContext(),
                        resources.getString(R.string.url_load_image) + it.backdropPath,
                        imageViewMoviePoster
                    )
                    setImage(
                        requireContext(),
                        resources.getString(R.string.url_load_image) + it.posterPath,
                        imageViewMovieFront
                    )
                    imageViewMovieFront.clipToOutline = true
                    textViewMovieDetailsTitle.text = it.name
                    textViewMovieDetailsScore.text = it.popularity.toString()
                    textViewMovieDetailsVote.text = it.rating.toString()
                    textViewMovieDescription.text = it.overview

                    recyclerRecommendation.layoutManager = recommendedShowsLayout
                    recyclerRecommendation.adapter = mRecommendationShowsAdapter
                }
                getRecommendationShow(it.id)
            }
        } else {
            showViewModel.currentRatedShowSelected.observe(viewLifecycleOwner) {
                binding.apply {
                    setImage(
                        requireContext(),
                        resources.getString(R.string.url_load_image) + it.backdropPath,
                        imageViewMoviePoster
                    )
                    setImage(
                        requireContext(),
                        resources.getString(R.string.url_load_image) + it.posterPath,
                        imageViewMovieFront
                    )
                    imageViewMovieFront.clipToOutline = true
                    textViewMovieDetailsTitle.text = it.name
                    textViewMovieDetailsScore.text = it.popularity.toString()
                    textViewMovieDetailsVote.text = it.rating.toString()
                    textViewMovieDescription.text = it.overview

                    recyclerRecommendation.layoutManager = recommendedRatedShowsLayout
                    recyclerRecommendation.adapter = mRecommendationRatedShowsAdapter
                }
                getRecommendationRatedShow(it.id)
            }
        }
    }

    private fun getRecommendationShow(id: Long) {
        showViewModel.viewModelScope.launch {
            MoviesRepository.getRecommendationShow(
                id,
                recommendedPage,
                onSuccess = ::onRecommendationShowFetched,
                onError = ::onError
            )
        }
    }

    private fun getRecommendationRatedShow(id: Long) {
        showViewModel.viewModelScope.launch {
            MoviesRepository.getRecommendationRatedShow(
                id,
                recommendedPage,
                onSuccess = ::onRecommendationRatedShowFetched,
                onError = ::onError
            )
        }
    }

    private fun onRecommendationShowFetched(shows: List<Show>) {
        mRecommendationShowsAdapter.appendMovies(shows)
        binding.button.setGoneVisibility(false)
    }

    private fun onRecommendationRatedShowFetched(shows: List<RatedShow>) {
        mRecommendationRatedShowsAdapter.appendMovies(shows)
        binding.button.setGoneVisibility(false)
    }

    private fun onError() {
        binding.button.setGoneVisibility(true)
    }

    companion object {
        val NAME: String = DetailsShowFragment::class.java.simpleName
        fun newInstance(showType: TvShows) = DetailsShowFragment(showType)
    }
}