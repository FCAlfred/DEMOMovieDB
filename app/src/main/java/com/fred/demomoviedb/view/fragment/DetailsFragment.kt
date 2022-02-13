package com.fred.demomoviedb.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fred.demomoviedb.R
import com.fred.demomoviedb.databinding.FragmentDetailsBinding
import com.fred.demomoviedb.model.Movie
import com.fred.demomoviedb.usecases.MoviesRepository
import com.fred.demomoviedb.utils.setGoneVisibility
import com.fred.demomoviedb.utils.setImage
import com.fred.demomoviedb.utils.setVisible
import com.fred.demomoviedb.view.adapter.MoviesAdapter
import com.fred.demomoviedb.viewModel.MovieViewModel
import es.dmoral.toasty.Toasty

class DetailsFragment : Fragment(), MoviesAdapter.MovieActions {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel: MovieViewModel by activityViewModels()
    private lateinit var mRecommendationsMoviesAdapter: MoviesAdapter
    private lateinit var recommendedMoviesLayout: LinearLayoutManager
    private var recommendedMoviePage: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        setObservers()
    }

    override fun onClickedMovie(selectedMovie: Movie) {
        movieViewModel.setMovieSelected(selectedMovie)
    }

    private fun initComponents() {
        var isRecyclerVisible = false
        mRecommendationsMoviesAdapter = MoviesAdapter(mutableListOf(), this@DetailsFragment)
        recommendedMoviesLayout = LinearLayoutManager(
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
        movieViewModel.currentMovieSelected.observe(viewLifecycleOwner) {
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
                textViewMovieDetailsTitle.text = it.title
                textViewMovieDetailsScore.text = it.popularity.toString()
                textViewMovieDetailsVote.text = it.rating.toString()
                textViewMovieDescription.text = it.overview

                recyclerRecommendation.layoutManager = recommendedMoviesLayout
                recyclerRecommendation.adapter = mRecommendationsMoviesAdapter
            }
            getRecommendationMovies(it.id)
        }
    }

    private fun getRecommendationMovies(id: Long) {
        MoviesRepository.getRecommendationMovies(
            id,
            recommendedMoviePage,
            onSuccess = ::onRecommendationMoviesFetched,
            onError = ::onError
        )
    }

    private fun onRecommendationMoviesFetched(movies: List<Movie>) {
        mRecommendationsMoviesAdapter.appendMovies(movies)
    }

    private fun onError() {
        Toasty.error(requireContext(), "NO CONNECTION", Toasty.LENGTH_LONG, true).show()
    }

    companion object {
        val NAME: String = DetailsFragment::class.java.simpleName
        fun newInstance() = DetailsFragment()
    }
}