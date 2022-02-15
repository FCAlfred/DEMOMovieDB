package com.fred.demomoviedb.view.shows.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fred.demomoviedb.R
import com.fred.demomoviedb.databinding.ItemMovieBinding
import com.fred.demomoviedb.model.Show
import com.fred.demomoviedb.utils.setImage

class ShowsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemMovieBinding.bind(view)

    fun onBindView(showInfo: Show) {
        binding.apply {
            setImage(
                view.context,
                view.resources.getString(R.string.url_load_image) + showInfo.posterPath,
                imageViewItemMovie
            )
            textViewItemTitle.text = showInfo.name
        }
    }
}