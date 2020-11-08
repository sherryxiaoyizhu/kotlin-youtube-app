package com.example.kotlinyoutube.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.kotlinyoutube.MainActivity.Companion.MY_SECRET_API_KEY
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.api.OnePlaylist
import com.google.android.youtube.player.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_youtube_player.*
import kotlinx.android.synthetic.main.content_youtube_player.*
import kotlinx.android.synthetic.main.content_youtube_player.commentsCountTV
import kotlinx.android.synthetic.main.content_youtube_player.dislikeCountTV
import kotlinx.android.synthetic.main.content_youtube_player.likeCountTV
import kotlinx.android.synthetic.main.content_youtube_player.oneVideoDescriptionTV
import kotlinx.android.synthetic.main.content_youtube_player.viewCount_publishedAt_TV
import okhttp3.*
import java.io.IOException

class YouTubeMediaPlayer: YouTubeBaseActivity() {

    // initialize viewModel
    //private val viewModel: MainViewModel by viewModels()
    private val viewModel = MainViewModel()

    companion object {
        var videoUrl = ""
        var videoId = ""

        const val WEB_URL_KEY = "WEB_URL"
        const val VIDEO_ID_KEY = "VIDEO_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        // action bar: go back arrow
        actionBack.setOnClickListener {
            finish()
        }

        youtubeBut.setOnClickListener {
            val intent = Intent(it.context, WebViewActivity::class.java).apply {
                putExtra(WEB_URL_KEY, videoUrl)
            }
            startActivity(intent)
        }

        // YouTube player
        youtubePlayer.initialize(MY_SECRET_API_KEY, object:YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                wasRestored: Boolean
            ) {
                Log.d("XXX", "YouTubePlayer onInitializationSuccess...")
                if (player == null) return
                if (wasRestored) {
                    player.play()
                } else {
                    player.cueVideo(videoId)
                    //player.loadVideo(videoId) // auto play
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                }
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.d("XXX", "YouTube Fragment Initialization Failed...")
            }
        })

        // get data through intent
        intent.extras?.apply {

            // update ActionBar title
            actionBarVideoTitle.text = getString(HomeAdapter.VH.VIDEO_TITLE_KEY)

            // get video id
            videoId = getString(VIDEO_ID_KEY).toString().substringBefore('/')

            // get video http url
            val httpUrl = "https://www.googleapis.com/youtube/v3/videos?"+
                    "id=${videoId}"+
                    "&key=$MY_SECRET_API_KEY"+
                    "&part=snippet,contentDetails,statistics,status"

            // Note: home fragment renders data from a YouTube video playlist,
            // here videoUrl is specified for each video
            videoUrl = "https://www.youtube.com/watch?v=${videoId}"

            // fetch JSON for video detail
            fetchJSON(httpUrl)
        }
    }

    private fun fetchJSON(url: String) {

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //Log.d("XXX", "Json parsed: $body")

                val gson = GsonBuilder().create()
                val onePlaylist = gson.fromJson(body, OnePlaylist::class.java)
                val item =  onePlaylist.items[0]

                runOnUiThread {

                    // fetch data
                    //val thumbnailUrl = item.snippet.thumbnails.high.url
                    val viewCount = item.statistics.viewCount
                    val publishedDate = item.snippet.publishedAt.substringBefore('T')
                    val likesCount = item.statistics.likeCount
                    val dislikesCount = item.statistics.dislikeCount
                    val commentsCount = item.statistics.commentCount
                    val description = item.snippet.description

                    // display video detail thumbnail
                    //val thumbnailImageView = videoDetailImageView
                    //Picasso.with(applicationContext).load(thumbnailUrl).into(thumbnailImageView)

                    // display video details: views, published time, number of likes, comments, description
                    val views = viewModel.getThousands(viewCount)
                    val time = viewModel.getTimeAgo(viewModel.stringToDate(publishedDate))
                    viewCount_publishedAt_TV.text = "$views views • $time"
                    likeCountTV.text = viewModel.getShortScale(likesCount)
                    dislikeCountTV.text = viewModel.getShortScale(dislikesCount)
                    commentsCountTV.text = viewModel.getShortScale(commentsCount)
                    oneVideoDescriptionTV.text = description

//                    videoDetailImageView.setOnLongClickListener {
//                        val intent = Intent(it.context, YouTubeMediaPlayer::class.java).apply {
//                            putExtra(VIDEO_ID_KEY, videoId)
//                        }
//                        startActivity(intent)
//                        true
//                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XXX", "Failed to execute request")
            }
        })
    }
}
