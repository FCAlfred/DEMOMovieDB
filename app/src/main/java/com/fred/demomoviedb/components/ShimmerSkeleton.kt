package com.fred.demomoviedb.components

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.fred.demomoviedb.R
import com.fred.demomoviedb.utils.setGoneVisibility

class ShimmerSkeleton(
    context: Context, attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    private lateinit var shimmerContainerOne: ShimmerFrameLayout
    private lateinit var shimmerContainerTwo: ShimmerFrameLayout

    init {
        inflate(context, R.layout.skeleton_shimmer, this)
        initComponents()
    }

    private fun initComponents() {
        shimmerContainerOne = findViewById(R.id.shimmer_container_one)
        shimmerContainerTwo = findViewById(R.id.shimmer_container_two)
    }

    fun hideShimmer() {
        shimmerContainerOne.setGoneVisibility(true)
        shimmerContainerOne.stopShimmer()
        shimmerContainerTwo.setGoneVisibility(true)
        shimmerContainerTwo.stopShimmer()
    }

    fun showShimmer() {
        shimmerContainerOne.setGoneVisibility(false)
        shimmerContainerOne.startShimmer()
        shimmerContainerTwo.setGoneVisibility(false)
        shimmerContainerTwo.startShimmer()
    }

}