package com.example.kotlinyoutube.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kotlinyoutube.R

class HomeFragment: Fragment() {

    private lateinit var swipe: SwipeRefreshLayout
    private val favoritesFragTag = "favoritesFragTag"
    private val videosFragTag = "videosFragTag"

    private val viewModel: MainViewModel by activityViewModels()

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_rv, container, false)
        initSwipeLayout(root)
        initAdapter(root)
        //actionFavorite()
        return root
    }

    private fun initSwipeLayout(root: View) {
        swipe = root.findViewById(R.id.swipeRefreshLayout)
        swipe.isRefreshing = true
        swipe.setOnRefreshListener {
            swipe.isRefreshing = true
            viewModel.repoFetch() // debugging...
        }
    }

    private fun initAdapter(root: View) {

        val viewAdapter = HomeAdapter(viewModel)
        root.findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        // observe data change
        viewModel.observeVideos().observe(viewLifecycleOwner,
            Observer {
                viewAdapter.submitList(it)
                viewAdapter.notifyDataSetChanged()
                swipe.isRefreshing = false
            })
    }

    private fun actionFavorite() {
        requireActivity().findViewById<ImageView>(R.id.actionFavorite)
            .setOnClickListener {
                val newFavFrag = Favorites.newInstance()
                parentFragmentManager.apply {
                    if (backStackEntryCount == 0) { // // only enables actionFavorite in home fragment
                        beginTransaction()
                            .add(R.id.main_frame, newFavFrag)
                            .addToBackStack(favoritesFragTag)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit()
                    }
                }
            }
    }
}
