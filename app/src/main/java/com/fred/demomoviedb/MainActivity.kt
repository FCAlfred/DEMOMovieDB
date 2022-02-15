package com.fred.demomoviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fred.demomoviedb.databinding.ActivityMainBinding
import com.fred.demomoviedb.view.adapter.ViewPagerAdapter
import com.fred.demomoviedb.view.movies.fragment.MoviesFragment
import com.fred.demomoviedb.view.shows.fragment.TvShowsFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragments: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        binding.viewPager2.isUserInputEnabled = false
    }

    private fun initComponents() {
        fragments = arrayListOf(
            MoviesFragment.newInstance(),
            TvShowsFragment.newInstance()
        )
        val mAdapter = ViewPagerAdapter(fragments, this)
        binding.viewPager2.adapter = mAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = MoviesFragment.NAME
                else -> tab.text = TvShowsFragment.NAME
            }
        }.attach()
    }
}