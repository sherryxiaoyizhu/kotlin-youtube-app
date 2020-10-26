package com.example.kotlinyoutube.api

import android.text.SpannableString
import com.example.kotlinyoutube.MainActivity.Companion.PLAYLIST_HTTP_URL
import com.example.kotlinyoutube.MainActivity.Companion.PLAYLIST_HTTPS_URL
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.reflect.Type
import okhttp3.logging.HttpLoggingInterceptor
import retrofit.GsonConverterFactory
import retrofit2.Converter

interface YouTubeApi {
    @GET(PLAYLIST_HTTPS_URL)
    suspend fun getPosts(): Playlist

    // Note: This class allows Retrofit to parse items in our model of type SpannableString.
    class SpannableDeserializer : JsonDeserializer<SpannableString> {
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): SpannableString {
            return SpannableString(json.asString)
        }
    }

    companion object {
        // Tell Gson to use our SpannableString deserializer
        private fun buildGsonConverterFactory(): GsonConverterFactory? {
            val gsonBuilder = GsonBuilder().registerTypeAdapter(
                SpannableString::class.java, SpannableDeserializer()
            )
            return GsonConverterFactory.create(gsonBuilder.create())
        }

        // base URL
        var httpurl = HttpUrl.Builder()
            .scheme("https")
            .host("www.googleapis.com")
            .build()

//        fun create(): YouTubeApi = create(httpurl)

//        private fun create(httpUrl: HttpUrl): YouTubeApi {
//            val client = OkHttpClient.Builder()
//                .addInterceptor(HttpLoggingInterceptor().apply {
//                    // Enable basic HTTP logging to help with debugging.
//                    this.level = HttpLoggingInterceptor.Level.BASIC
//                })
//                .build()
//            return Retrofit.Builder()
//                .baseUrl(httpUrl)
//                .client(client)
//                .addConverterFactory(buildGsonConverterFactory()) //
//                .build()
//                .create(YouTubeApi::class.java)
//        }
    }

    // ***************** //
    // *** JSON DATA *** //
    // ***************** //

    // ** parse Json: HomeFragment **
    class Playlist(val items: List<Video>)

    class Video(val snippet: Snippet)

    class Snippet(val publishedAt: String, val title: String, val description: String,
                  val channelTitle: String, val position: Int, val thumbnails: Thumbnails)

    // ** parse Json: OneVideoActivity **
    class OnePlaylist(val items: List<OneVideo>)

    class OneVideo(val snippet: OneSnippet, val statistics: Statistics)

    class OneSnippet(val publishedAt: String, val title: String, val description: String,
                     val channelTitle: String, val position: Int, val thumbnails: Thumbnails)

    class Statistics(val viewCount: String, val likeCount: String, val dislikeCount: String, val commentCount: String)

    // ** shared models **
    class Thumbnails(val standard: Standard)

    class Standard(val url: String)
}

