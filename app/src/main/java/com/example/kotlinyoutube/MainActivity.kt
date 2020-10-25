package com.example.kotlinyoutube

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kotlinyoutube.api.Playlist
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.*
import java.io.IOException
import java.util.Observer

class MainActivity: AppCompatActivity() {

    companion object {
        const val MY_SECRET_API_KEY = "AIzaSyArKlbgIq5WSsDxoo2AFc4JD4qRAiJf1Xs" // update and put it in .env before publishing the repo
    }

    private lateinit var swipe: SwipeRefreshLayout

    // initialize viewModel
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set up tool bar
        setSupportActionBar(mainToolbar)

        // inflate content_main and initialize swipe refresh layout
        val root = layoutInflater.inflate(R.layout.content_main, null)
        initSwipeLayout(root)
        //initAdapter(root)

        // set up recycler view
        recyclerView_main.layoutManager = LinearLayoutManager(this) // ***

        // fetch JSON for video playlist
        fetchJSON()
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
                "&key=$MY_SECRET_API_KEY" +
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
                runOnUiThread {

                    // initialize adapter
                    val viewAdapter = MainAdapter(playlist, viewModel)
                    recyclerView_main.apply {
                        adapter = viewAdapter // ***
                        layoutManager = LinearLayoutManager(this.context)
                    }
//                    viewModel.observeVideos().observe(viewLifecycleOwner,
//                        Observer {
//                            viewAdapter.submitList(it)
//                            viewAdapter.notifyDataSetChanged()
//                            swipe.isRefreshing = false
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
