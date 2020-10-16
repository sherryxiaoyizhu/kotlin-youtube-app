package com.example.kotlinyoutube

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.video_detail.*
import okhttp3.*
import java.io.IOException

class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_detail)

        // set up recycler view
        recyclerView_video_detail.layoutManager = LinearLayoutManager(this)

        // display ActionBar back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // update ActionBar title
        val navBarTitle = intent.getStringExtra(CustomViewHolder.VIDEO_TITLE_KEY)
        supportActionBar?.title = navBarTitle

        fetchJson()
    }

    private fun fetchJson() {
        // get video url
        val MY_SECRET_API_KEY = "AIzaSyArKlbgIq5WSsDxoo2AFc4JD4qRAiJf1Xs" // update and put it in .env before publishing the repo
        val videoId = intent.getStringExtra(CustomViewHolder.VIDEO_ID_KEY).toString().substringBefore('/')
        val videoUrl = "https://www.googleapis.com/youtube/v3/videos?"+
                "id=$videoId"+
                "&key=$MY_SECRET_API_KEY"+
                "&part=snippet,contentDetails,statistics,status"

        val request = Request.Builder().url(videoUrl).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //Log.d("XXX", "Json parsed: $body")

                val gson = GsonBuilder().create()
                val playlistDetail = gson.fromJson(body, PlaylistDetail::class.java)

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