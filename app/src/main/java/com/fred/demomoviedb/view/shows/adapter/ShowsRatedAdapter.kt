package com.fred.demomoviedb.view.shows.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fred.demomoviedb.R
import com.fred.demomoviedb.model.RatedShow
import com.fred.demomoviedb.model.Show

class ShowsRatedAdapter(
    private var shows: MutableList<RatedShow>,
    private val callback: ShowsActions
) : RecyclerView.Adapter<ShowsRatedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsRatedViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ShowsRatedViewHolder(view)
    }

    override fun getItemCount(): Int = shows.size

    fun appendMovies(showList: List<RatedShow>) {
        this.shows.addAll(showList)
        notifyItemRangeInserted(this.shows.size, showList.size - 1)
    }

    override fun onBindViewHolder(holder: ShowsRatedViewHolder, position: Int) {
        holder.apply {
            onBindView(shows[position])
            itemView.findViewById<ImageView>(R.id.image_view_item_movie).clipToOutline = true
            itemView.setOnClickListener {
                callback.onClickedRatedShow(shows[position])
            }
        }
    }

    interface ShowsActions {
        fun onClickedRatedShow(selectedShow: RatedShow)
    }
}