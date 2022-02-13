package com.fred.demomoviedb.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.fred.demomoviedb.R
import com.fred.demomoviedb.databinding.FragmentDetailsBinding
import com.fred.demomoviedb.utils.setImage
import com.fred.demomoviedb.viewModel.MovieViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
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
            }
        }
    }

    companion object {
        val NAME: String = DetailsFragment::class.java.simpleName
        fun newInstance() = DetailsFragment()
    }
}