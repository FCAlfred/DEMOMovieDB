package com.fred.demomoviedb.view.shows.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.fred.demomoviedb.R
import com.fred.demomoviedb.components.TvShows
import com.fred.demomoviedb.databinding.FragmentTvShowsBinding
import com.fred.demomoviedb.model.RatedShow
import com.fred.demomoviedb.model.Show
import com.fred.demomoviedb.usecases.local.MoviesDb
import com.fred.demomoviedb.usecases.network.MoviesRepository
import com.fred.demomoviedb.utils.add
import com.fred.demomoviedb.utils.setVisible
import com.fred.demomoviedb.view.shows.adapter.ShowsAdapter
import com.fred.demomoviedb.view.shows.adapter.ShowsRatedAdapter
import com.fred.demomoviedb.viewModel.MovieViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

class TvShowsFragment : Fragment(), ShowsAdapter.ShowsActions, ShowsRatedAdapter.ShowsActions {

    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = _binding!!
    private val showViewModel: MovieViewModel by activityViewModels()

    private lateinit var mPopularShowsAdapter: ShowsAdapter
    private lateinit var popularShowsLayout: LinearLayoutManager
    private var popularShowPage: Int = 1

    private lateinit var mRatedShowsAdapter: ShowsRatedAdapter
    private lateinit var ratedShowsLayout: LinearLayoutManager
    private var ratedShowPage: Int = 1

    private lateinit var localDb: MoviesDb

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    override fun onClickedShow(selectedShow: Show) {
        showViewModel.setShowSelected(selectedShow)
        requireActivity().supportFragmentManager.add(
            R.id.main_container,
            DetailsShowFragment.newInstance(TvShows.POPULAR),
            DetailsShowFragment.NAME
        )
    }

    override fun onClickedRatedShow(selectedShow: RatedShow) {
        showViewModel.setRatedShowSelected(selectedShow)
        requireActivity().supportFragmentManager.add(
            R.id.main_container,
            DetailsShowFragment.newInstance(TvShows.RATED),
            DetailsShowFragment.NAME
        )
    }

    private fun initComponents() {
        localDb = Room.databaseBuilder(
            requireContext(),
            MoviesDb::class.java,
            "PopularShows"
        ).build()
        popularShowsLayout = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        ratedShowsLayout = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.apply {
            recyclerPopularShows.layoutManager = popularShowsLayout
            mPopularShowsAdapter = ShowsAdapter(mutableListOf(), this@TvShowsFragment)
            recyclerPopularShows.adapter = mPopularShowsAdapter

            recyclerRatedShows.layoutManager = ratedShowsLayout
            mRatedShowsAdapter = ShowsRatedAdapter(mutableListOf(), this@TvShowsFragment)
            recyclerRatedShows.adapter = mRatedShowsAdapter

            reconnectingShow.setOnClickListener {
                it.setVisible(false)
                shimmerShows.showShimmer()
                imageViewShowsConnectionStatus.setVisible(false)
                getPopularShows()
                getRatedShows()
            }
        }
        getPopularShows()
        getRatedShows()
    }

    private fun getPopularShows() {
        showViewModel.viewModelScope.launch {
            MoviesRepository.getPopularShows(
                popularShowPage,
                onSuccess = ::onPopularShowsFetched,
                onError = ::onError
            )
        }
    }

    private fun getRatedShows() {
        showViewModel.viewModelScope.launch {
            MoviesRepository.getRatedShows(
                ratedShowPage,
                onSuccess = ::onTopRatedShowsFetched,
                onError = ::onError
            )
        }
    }

    private fun onPopularShowsFetched(shows: List<Show>) {
        showViewModel.viewModelScope.launch {
            if (localDb.showDao().getAllShows().isNullOrEmpty()) {
                localDb.showDao().insertShowList(shows)
            } else {
                localDb.showDao().updateShowList(shows)
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            binding.apply {
                shimmerShows.hideShimmer()
                mainContainerShowsList.setVisible(true)
            }
            mPopularShowsAdapter.appendMovies(shows)
            attachPopularShowsOnScrollListener()
        }, 1500)
    }

    private fun onTopRatedShowsFetched(shows: List<RatedShow>) {
        showViewModel.viewModelScope.launch {
            if (localDb.ratedShowDao().getAllRatedShows().isNullOrEmpty()) {
                localDb.ratedShowDao().insertRatedShowsList(shows)
            } else {
                localDb.ratedShowDao().updateRatedShowsList(shows)
            }
        }
        mRatedShowsAdapter.appendMovies(shows)
        attachTopRatedShowsOnScrollListener()
    }

    private fun attachPopularShowsOnScrollListener() {
        binding.recyclerPopularShows.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularShowsLayout.itemCount
                val visibleItemCount = popularShowsLayout.childCount
                val firstVisibleItem = popularShowsLayout.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.recyclerPopularShows.removeOnScrollListener(this)
                    popularShowPage++
                    getPopularShows()
                }
            }
        })
    }

    private fun attachTopRatedShowsOnScrollListener() {
        binding.recyclerRatedShows.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = ratedShowsLayout.itemCount
                val visibleItemCount = ratedShowsLayout.childCount
                val firstVisibleItem = ratedShowsLayout.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.recyclerRatedShows.removeOnScrollListener(this)
                    ratedShowPage++
                    getRatedShows()
                }
            }
        })
    }

    private fun onError() {
        validateLocalDb()
    }

    private fun validateLocalDb() {
        showViewModel.viewModelScope.launch {
            val localShowList = localDb.showDao().getAllShows()
            val localRatedShowsList = localDb.ratedShowDao().getAllRatedShows()
            Handler(Looper.getMainLooper()).postDelayed({
                if (localShowList.isNullOrEmpty()) {
                    Toasty.info(
                        requireContext(),
                        getString(R.string.no_internet_connection),
                        Toasty.LENGTH_SHORT,
                        true
                    ).show()
                    binding.apply {
                        shimmerShows.hideShimmer()
                        imageViewShowsConnectionStatus.setVisible(true)
                        reconnectingShow.setVisible(true)
                        mainContainerShowsList.setVisible(false)
                    }
                } else {
                    binding.apply {
                        shimmerShows.hideShimmer()
                        mainContainerShowsList.setVisible(true)
                    }
                    mPopularShowsAdapter.appendMovies(localShowList)
                    mRatedShowsAdapter.appendMovies(localRatedShowsList)
                    attachPopularShowsOnScrollListener()
                    attachTopRatedShowsOnScrollListener()
                    Toasty.info(
                        requireContext(),
                        getString(R.string.offline_mode),
                        Toasty.LENGTH_SHORT,
                        true
                    ).show()
                }
            }, 2000)
        }
    }

    companion object {
        const val NAME = "TvShows"
        fun newInstance() = TvShowsFragment()
    }
}