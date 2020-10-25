package com.example.kotlinyoutube.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kotlinyoutube.MainActivity
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.api.Playlist
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_rv.*
import okhttp3.*
import java.io.IOException

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
        // fetch JSON for video playlist
        fetchJSON()
        return root
    }

    private fun initSwipeLayout(root: View) {
        swipe = root.findViewById(R.id.swipeRefreshLayout)
        swipe.isRefreshing = true
        swipe.setOnRefreshListener {
            swipe.isRefreshing = true
            viewModel.repoFetch()
        }
    }

    fun fetchJSON(): String {

        var videos = ""

        // get playlist url
        val httpurl = "https://www.googleapis.com/youtube/v3/playlistItems?&maxResults=100" +
                "&playlistId=PLx0sYbCqOb8TBPRdmBHs5Iftvv9TPboYG" +
                "&key=${MainActivity.MY_SECRET_API_KEY}" +
                "&fields=items(snippet(publishedAt,title,description,channelTitle,position,thumbnails))" +
                "&part=snippet"

        val request = Request.Builder().url(httpurl).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //Log.d("XXX", "Json parsed: $body")
                videos = body!!

                val gson = GsonBuilder().create()
                val playlist = gson.fromJson(body, Playlist::class.java)
                // https://stackoverflow.com/questions/57330607/kotlin-runonuithread-unresolved-reference
                activity?.runOnUiThread {
                    // initialize adapter
                    val viewAdapter = MainAdapter(
                        playlist,
                        viewModel
                    )
                    recyclerView.apply {
                        adapter = viewAdapter // ***
                        layoutManager = LinearLayoutManager(this.context)
                    }
//                    viewModel.observeVideos().observe(viewLifecycleOwner, Observer {
//                        viewAdapter.submitList(it)
//                        viewAdapter.notifyDataSetChanged()
//                        swipe.isRefreshing = false
//                    })
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XXX", "Failed to execute request")
            }
        })
        return videos
    }
}
