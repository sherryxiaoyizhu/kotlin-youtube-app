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
import com.example.kotlinyoutube.MainActivity
import com.example.kotlinyoutube.MainActivity.Companion.PLAYLIST_HTTP_URL
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.api.OneVideo
import com.example.kotlinyoutube.api.Playlist
import com.example.kotlinyoutube.api.Video
import com.example.kotlinyoutube.api.YouTubeApi
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_rv.*
import okhttp3.*
import java.io.IOException
import java.lang.System.exit

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
                //Log.d("XXX", "observe() called in initAdapter()")
                viewAdapter.submitList(it)
                viewAdapter.notifyDataSetChanged()
                swipe.isRefreshing = false
            })
    }

//    fun fetchJSON(): List<Video> {
//
//        var videoList = listOf<Video>()
//
//        // get playlist url
//        val httpurl = PLAYLIST_HTTP_URL
//        val request = Request.Builder().url(httpurl).build()
//        val client = OkHttpClient()
//
//        client.newCall(request).enqueue(object: Callback {
//            override fun onResponse(call: Call, response: Response) {
//                val body = response.body?.string()
//                //Log.d("XXX", "Json parsed: $body")
//
//                val gson = GsonBuilder().create()
//                val playlist = gson.fromJson(body, Playlist::class.java)
//                videoList = playlist.items
//
//                //Log.d("XXX", "videoList = $videoList")
//
//                // https://stackoverflow.com/questions/57330607/kotlin-runonuithread-unresolved-reference
//                activity?.runOnUiThread {
//                    // initialize adapter (initAdapter)
//                    val viewAdapter = HomeAdapter(videoList, viewModel)
//                    recyclerView.apply {
//                        adapter = viewAdapter
//                        layoutManager = LinearLayoutManager(this.context)
//                    }
//                    // observe data change
//                    viewModel.observeVideos().observe(viewLifecycleOwner,
//                        Observer {
//                            Log.d("XXX", "observe() called in initAdapter")
//                            viewAdapter.submitList(it)
//                            viewAdapter.notifyDataSetChanged()
//                            swipe.isRefreshing = false
//                            //exit(0)
//                    })
//                }
//            }
//
//            override fun onFailure(call: Call, e: IOException) {
//                Log.d("XXX", "Failed to execute request")
//            }
//        })
//        return videoList
//    }

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
