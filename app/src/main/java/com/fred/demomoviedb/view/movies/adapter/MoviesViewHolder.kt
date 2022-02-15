package com.fred.demomoviedb.view.movies.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fred.demomoviedb.R
import com.fred.demomoviedb.databinding.ItemMovieBinding
import com.fred.demomoviedb.model.Movie
import com.fred.demomoviedb.utils.setImage

class MoviesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemMovieBinding.bind(view)

    fun onBindView(movieInfo: Movie) {
        binding.apply {
            setImage(
                view.context,
                view.resources.getString(R.string.url_load_image) + movieInfo.posterPath,
                imageViewItemMovie
            )
            textViewItemTitle.text = movieInfo.title
        }
    }
}