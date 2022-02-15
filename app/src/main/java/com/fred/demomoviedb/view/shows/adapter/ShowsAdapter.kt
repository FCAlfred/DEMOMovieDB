package com.fred.demomoviedb.view.shows.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fred.demomoviedb.R
import com.fred.demomoviedb.model.Show

class ShowsAdapter(
    private var shows: MutableList<Show>,
    private val callback: ShowsActions
) : RecyclerView.Adapter<ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ShowsViewHolder(view)
    }

    override fun getItemCount(): Int = shows.size

    fun appendMovies(showList: List<Show>) {
        this.shows.addAll(showList)
        notifyItemRangeInserted(this.shows.size, showList.size - 1)
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.apply {
            onBindView(shows[position])
            itemView.findViewById<ImageView>(R.id.image_view_item_movie).clipToOutline = true
            itemView.setOnClickListener {
                callback.onClickedShow(shows[position])
            }
        }
    }

    interface ShowsActions {
        fun onClickedShow(selectedShow: Show)
    }
}