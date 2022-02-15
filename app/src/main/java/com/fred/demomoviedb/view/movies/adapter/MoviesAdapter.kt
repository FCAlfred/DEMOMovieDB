package com.fred.demomoviedb.view.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fred.demomoviedb.R
import com.fred.demomoviedb.model.Movie

class MoviesAdapter(
    private var movies: MutableList<Movie>,
    private val callback: MovieActions
) : RecyclerView.Adapter<MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MoviesViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    fun appendMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(this.movies.size, movies.size - 1)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.apply {
            onBindView(movies[position])
            itemView.findViewById<ImageView>(R.id.image_view_item_movie).clipToOutline = true
            itemView.setOnClickListener {
                callback.onClickedMovie(movies[position])
            }
        }
    }

    interface MovieActions {
        fun onClickedMovie(selectedMovie: Movie)
    }
}