package com.example.kotlinyoutube.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinyoutube.MainActivity.Companion.MY_SECRET_API_KEY
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.api.OnePlaylist
import com.example.kotlinyoutube.api.OneVideo
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_one_video.*
import kotlinx.android.synthetic.main.content_one_video_rv.*
import okhttp3.*
import java.io.IOException

class OneVideoActivity: AppCompatActivity() {

    // initialize viewModel
    private val viewModel: MainViewModel by viewModels()

    companion object {
        var videoUrl = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_video)
        // set up tool bar
        setSupportActionBar(toolbar)
        // enable ActionBar back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // set up recycler view
        recyclerView_one_video.layoutManager = LinearLayoutManager(this) // ***

        // get data through intent
        intent.extras?.apply {

            // update ActionBar title
            val navBarTitle = getString(CustomViewHolder.VIDEO_TITLE_KEY)
            supportActionBar?.title = navBarTitle

            // get video url
            val videoId = getString(CustomViewHolder.VIDEO_ID_KEY).toString().substringBefore('/')
            val httpUrl = "https://www.googleapis.com/youtube/v3/videos?"+
                    "id=$videoId"+
                    "&key=$MY_SECRET_API_KEY"+
                    "&part=snippet,contentDetails,statistics,status"

            videoUrl = "https://www.youtube.com/watch?v=$videoId"

            // fetch JSON for video detail
            fetchJSON(httpUrl)
        }
    }

    fun fetchJSON(url: String): List<OneVideo> {

        var videoList = listOf<OneVideo>()
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //Log.d("XXX", "Json parsed: $body")

                val gson = GsonBuilder().create()
                val onePlaylist = gson.fromJson(body, OnePlaylist::class.java)
                videoList = onePlaylist.items

                runOnUiThread {
                    recyclerView_one_video.adapter = OneVideoAdapter(onePlaylist, viewModel) // ***
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XXX", "Failed to execute request")
            }
        })
        return videoList
    }
}
