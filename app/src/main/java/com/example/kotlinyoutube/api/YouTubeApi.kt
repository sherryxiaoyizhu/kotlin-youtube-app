package com.example.kotlinyoutube.api

import android.text.SpannableString
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
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

interface YouTubeApi {
    //@GET(PLAYLIST_HTTPS_URL)
    //suspend fun getPlaylist(): Playlist

    //class Playlist(val items: List<VideoResponse>)
    //data class VideoResponse (val data: Video)

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

        fun create(): YouTubeApi = create(httpurl)

        private fun create(httpUrl: HttpUrl): YouTubeApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    // Enable basic HTTP logging to help with debugging.
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(buildGsonConverterFactory()) //  note the version of okhttp3
                .build()
                .create(YouTubeApi::class.java)
        }
    }
}
