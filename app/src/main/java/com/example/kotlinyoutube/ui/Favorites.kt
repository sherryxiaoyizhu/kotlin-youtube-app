package com.example.kotlinyoutube.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.api.OnePlaylist

class Favorites: Fragment() {
    // initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()

    companion object {
        fun newInstance(): Favorites {
            return Favorites()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // update the tool bar title
        viewModel.setTitle("Favorites")
        // inflate fragment recycler view
        val root = inflater.inflate(R.layout.content_one_video_rv, container, false)
        root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).isEnabled = false
        initAdapter(root)

        // back button
        requireActivity().onBackPressedDispatcher
            .addCallback(this) {
                //viewModel.setTitleToSubreddit()
                parentFragmentManager.popBackStack()
            }
        return root
    }

    // Set up the adapter
    private fun initAdapter(root: View) {
//        val onePlaylist: OnePlaylist
//        val viewAdapter = OneVideoAdapter(onePlaylist, viewModel)
//        root.findViewById<RecyclerView>(R.id.recyclerView_one_video).apply {
//            adapter = viewAdapter
//            layoutManager = LinearLayoutManager(root.context)
//        }
//
//        viewModel.observeFavorites().observe(viewLifecycleOwner, Observer {
//            //viewAdapter.submitList(it)
//            viewAdapter.notifyDataSetChanged()
//        })
    }
}