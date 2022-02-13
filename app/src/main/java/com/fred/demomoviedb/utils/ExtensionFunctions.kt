package com.fred.demomoviedb.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

fun setImage(context: Context, url: String, image: ImageView) {
    Glide.with(context)
        .load(url)
        .into(image)
}

fun View.setVisible(status: Boolean) {
    this.visibility = if (status) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun View.setGoneVisibility(status: Boolean) {
    this.visibility = if (status) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

fun FragmentManager.add(
    container: Int,
    fragmentInstance: Fragment,
    fragmentName: String? = null
) {
    this.beginTransaction()
        .add(container, fragmentInstance)
        .addToBackStack(fragmentName)
        .commit()
}