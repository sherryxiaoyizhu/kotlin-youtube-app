package com.example.kotlinyoutube.api

import android.text.SpannableString
import android.util.Log
import com.example.kotlinyoutube.MainActivity
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class Repository(private val api: YouTubeApi) {

    companion object {
        var playlist = Playlist(listOf())
        var flag = 1 // async flag
    }

    var gson = GsonBuilder().registerTypeAdapter(
        SpannableString::class.java, YouTubeApi.SpannableDeserializer()).create()

    private fun unpackVideos(response: Playlist): List<Video> {
        return response.items
    }

    fun getPlaylist(): List<Video> {

        val httpurl = MainActivity.PLAYLIST_HTTP_URL
        val request = Request.Builder().url(httpurl).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //Log.d("XXX", "Json parsed: $body")
                playlist = gson.fromJson(body, Playlist::class.java)
                flag = 2
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XXX", "Failed to execute request")
            }
        })

        // Wait until gson gets returned ...
        // https://kotlinlang.org/docs/tutorials/coroutines/async-programming.html
        while (flag == 1) {
            if (flag == 2) {
                break
            }
        }
        return unpackVideos(playlist)
    }
}
