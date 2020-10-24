package com.example.kotlinyoutube

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.*
import java.io.IOException

class MainActivity: AppCompatActivity() {

    // initialize viewModel
    private val viewModel: MainViewModel by viewModels()

    companion object {
        const val MY_SECRET_API_KEY = "AIzaSyArKlbgIq5WSsDxoo2AFc4JD4qRAiJf1Xs" // update and put it in .env before publishing the repo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // set up tool bar
        setSupportActionBar(mainToolbar)
        // set up recycler view
        recyclerView_main.layoutManager = LinearLayoutManager(this) // ***

        // fetch JSON for video list
        fetchJSON()
    }

    private fun fetchJSON() {
        // get playlist url
        val url = "https://www.googleapis.com/youtube/v3/playlistItems?&maxResults=100" +
                "&playlistId=PLx0sYbCqOb8TBPRdmBHs5Iftvv9TPboYG" +
                "&key=$MY_SECRET_API_KEY" +
                "&fields=items(snippet(publishedAt,title,description,channelTitle,position,thumbnails))" +
                "&part=snippet"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //Log.d("XXX", "Json parsed: $body")

                val gson = GsonBuilder().create()
                val playlist = gson.fromJson(body, Playlist::class.java)
                runOnUiThread {
                    recyclerView_main.adapter = MainAdapter(playlist, viewModel) // ***
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XXX", "Failed to execute request")
            }
        })
    }
}