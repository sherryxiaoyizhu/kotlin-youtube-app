package com.example.kotlinyoutube.api

import android.text.SpannableString
import android.util.Log
import com.example.kotlinyoutube.MainActivity
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class Repository(private val api: YouTubeApi) {

    companion object {
        lateinit var playlist: Playlist

        var gson = GsonBuilder().registerTypeAdapter(
            SpannableString::class.java, YouTubeApi.SpannableDeserializer()).create()
        //val gson = GsonBuilder().create()
    }

    private fun unpackVideos(response: YouTubeApi.Playlist): List<YouTubeApi.Snippet> {
        return response.items.map { it.snippet }
    }

//    suspend fun getPlaylist(): List<Snippet> {
//
//        if (MainActivity.globalDebug) {
//            val httpurl = MainActivity.PLAYLIST_HTTP_URL
//            val request = Request.Builder().url(httpurl).build()
//            val client = OkHttpClient()
//
//            client.newCall(request).enqueue(object : Callback {
//
//                override fun onResponse(call: Call, response: Response) {
//                    val body = response.body?.string()
//                    //Log.d("XXX", "Json parsed: $body")
//                    playlist = gson.fromJson(body, YouTubeApi.Playlist::class.java)
//                }
//
//                override fun onFailure(call: Call, e: IOException) {
//                    Log.d("XXX", "Failed to execute request")
//                }
//            })
//            return unpackVideos(playlist)
//        }
//        else {
//            return unpackVideos(api.getPlaylist())
//        }
//    }
}