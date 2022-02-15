package com.fred.demomoviedb.view.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fred.demomoviedb.R
import com.fred.demomoviedb.model.Movie
import com.fred.demomoviedb.model.RatedMovie

class MoviesRatedAdapter(
    private var movies: MutableList<RatedMovie>,
    private val callback: MovieActions
) : RecyclerView.Adapter<MoviesRatedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesRatedViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MoviesRatedViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    fun appendMovies(movies: List<RatedMovie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(this.movies.size, movies.size - 1)
    }

    override fun onBindViewHolder(holder: MoviesRatedViewHolder, position: Int) {
        holder.apply {
            onBindView(movies[position])
            itemView.findViewById<ImageView>(R.id.image_view_item_movie).clipToOutline = true
            itemView.setOnClickListener {
                callback.onClickedRatedMovie(movies[position])
            }
        }
    }

    interface MovieActions {
        fun onClickedRatedMovie(selectedMovie: RatedMovie)
    }
}