package com.example.kotlinyoutube

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_one_video.*
import kotlinx.android.synthetic.main.content_one_video.*
import okhttp3.*
import java.io.IOException

class OneVideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_video)

        // set up tool bar
        setSupportActionBar(toolbar)

        // set up recycler view
        recyclerView_video_detail.layoutManager = LinearLayoutManager(this)

        // display ActionBar back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // update ActionBar title
        val navBarTitle = intent.getStringExtra(CustomViewHolder.VIDEO_TITLE_KEY)
        supportActionBar?.title = navBarTitle

        // get video url
        val MY_SECRET_API_KEY = "AIzaSyArKlbgIq5WSsDxoo2AFc4JD4qRAiJf1Xs" // update and put it in .env before publishing the repo
        val videoId = intent.getStringExtra(CustomViewHolder.VIDEO_ID_KEY).toString().substringBefore('/')
        val url = "https://www.googleapis.com/youtube/v3/videos?"+
                "id=$videoId"+
                "&key=$MY_SECRET_API_KEY"+
                "&part=snippet,contentDetails,statistics,status"

        // fetch JSON for video detail
        fetchJson(url)
    }

    private fun fetchJson(url: String) {
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //Log.d("XXX", "Json parsed: $body")

                val gson = GsonBuilder().create()
                val playlistDetail = gson.fromJson(body, OnePlaylist::class.java)
                runOnUiThread {
                    recyclerView_video_detail.adapter = VideoAdapter(playlistDetail)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XXX", "Failed to execute request")
            }
        })
    }
}