package com.example.kotlinyoutube.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.example.kotlinyoutube.MainActivity.Companion.MY_SECRET_API_KEY
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.api.OnePlaylist
import com.example.kotlinyoutube.api.OneVideo
import com.example.kotlinyoutube.ui.HomeAdapter.VH.Companion.VIDEO_ID_KEY
import com.example.kotlinyoutube.ui.HomeAdapter.VH.Companion.VIDEO_TITLE_KEY
import com.google.android.youtube.player.*
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_youtube_player.*
import kotlinx.android.synthetic.main.content_youtube_player.*
import okhttp3.*
import java.io.IOException

class YouTubeMediaPlayer: YouTubeBaseActivity() {

    // initialize viewModel
    //private val viewModel: MainViewModel by viewModels()
    private val viewModel = MainViewModel()

    private var isExpanded = false
    // current video
    private var httpUrl = ""
    private var videoWebUrl = ""
    private var videoTitle = ""
    private var videoId = ""
    // next video
    private var nextImageUrl = ""
    private var nextTitle = ""
    private var nextDisplayText = ""
    private var nextVideoId = ""

    companion object {
        // pass to WebView
        const val WEB_URL_KEY = "WEB_URL"
        // current video
        const val CURRENT_VIDEO_TITLE_KEY = "CURRENT_VIDEO_TITLE"
        const val CURRENT_VIDEO_ID_KEY = "CURRENT_VIDEO_ID"
        // next video
        const val NEXT_VIDEO_URL_KEY = "NEXT_VIDEO_URL"
        const val NEXT_VIDEO_TITLE_KEY = "NEXT_VIDEO_TITLE"
        const val NEXT_DISPLAY_TEXT_KEY = "NEXT_DISPLAY_TEXT"
        const val NEXT_VIDEO_ID_KEY = "NEXT_VIDEO_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        initActionBar()
        initYouTubePlayer()
        getData()
    }

    private fun initActionBar() {
        // action bar: go back arrow
        actionBack.setOnClickListener {
            finish()
        }

        // YouTube search icon: direct to YouTube web page
        youtubeBut.setOnClickListener {
            val intent = Intent(it.context, WebViewActivity::class.java).apply {
                putExtra(WEB_URL_KEY, videoWebUrl)
            }
            startActivity(intent)
        }
    }

    private fun initYouTubePlayer() {
        youtubePlayer.initialize(MY_SECRET_API_KEY, object:YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                wasRestored: Boolean
            ) {
                if (player == null) {
                    Log.d("XXX", "player == null...")
                    return
                }
                if (wasRestored) {
                    player.play()
                } else {
                    player.cueVideo(videoId)
                    //player.loadVideo(videoId) // auto play
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                }
            }

            override fun onInitializationFailure(
                p0: com.google.android.youtube.player.YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.d("XXX", "YouTube Fragment Initialization Failed...")
            }
        })
    }

    private fun getData() {
        intent.extras?.apply {
            // update ActionBar title
            videoTitle = getString(VIDEO_TITLE_KEY)!!
            actionBarVideoTitle.text = videoTitle

            // get video id
            videoId = getString(VIDEO_ID_KEY).toString().substringBefore('/')

            // get video http url to fetch Json
            httpUrl = "https://www.googleapis.com/youtube/v3/videos?"+
                    "id=${videoId}"+
                    "&key=$MY_SECRET_API_KEY"+
                    "&part=snippet,contentDetails,statistics,status"

            // Note: home fragment renders data from a YouTube video playlist,
            // here videoUrl is specified for each video
            videoWebUrl = "https://www.youtube.com/watch?v=${videoId}"

            // get next video data
            nextImageUrl = getString(NEXT_VIDEO_URL_KEY)!!
            nextTitle = getString(NEXT_VIDEO_TITLE_KEY)!!
            nextDisplayText = getString(NEXT_DISPLAY_TEXT_KEY)!!
            nextVideoId = getString(NEXT_VIDEO_ID_KEY)!!
        }

        // fetch JSON for video detail
        fetchJSON(httpUrl)
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
                    initViews(item)
                    initChevron()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("XXX", "Failed to execute request")
            }
        })
    }

    private fun initViews(item: OneVideo) {
        // fetch data
        val viewCount = item.statistics.viewCount
        val publishedDate = item.snippet.publishedAt.substringBefore('T')
        val likesCount = item.statistics.likeCount
        val dislikesCount = item.statistics.dislikeCount
        val commentsCount = item.statistics.commentCount
        val description = item.snippet.description

        // display video details: views, published time, number of likes, comments, description
        videoTitleTV.text = videoTitle

        val views = viewModel.getThousands(viewCount)
        val time = viewModel.getTimeAgo(viewModel.stringToDate(publishedDate))
        viewCount_publishedAt_TV.text = "$views views • $time"

        likeCountTV.text = viewModel.getShortScale(likesCount)
        dislikeCountTV.text = viewModel.getShortScale(dislikesCount)
        commentsCountTV.text = viewModel.getShortScale(commentsCount)
        Picasso.with(applicationContext).load(nextImageUrl).into(nextVideoIVOne)
        nextTitleTVOne.text = nextTitle
        displayTVOne.text = nextDisplayText
        descriptionTV.text = description

        onClickNext()
    }

    private fun onClickNext() {
        nextVideoIVOne.setOnClickListener {
            val intent = Intent(it.context, YouTubeMediaPlayer::class.java).apply {
                putExtra(CURRENT_VIDEO_TITLE_KEY, videoTitle)
                putExtra(CURRENT_VIDEO_ID_KEY, videoId)
                putExtra(NEXT_VIDEO_URL_KEY, nextImageUrl)
                putExtra(NEXT_VIDEO_TITLE_KEY, nextTitle)
                putExtra(NEXT_DISPLAY_TEXT_KEY, nextDisplayText)
                putExtra(NEXT_VIDEO_ID_KEY, nextVideoId)
            }
            startActivity(intent)
        }
    }

    private fun initChevron() {
        chevron.setOnClickListener {
            if (isExpanded) {
                isExpanded = false
                chevron.setImageResource(R.drawable.ic_baseline_chevron_left_24dp)
                descriptionTV.visibility = GONE
            } else {
                isExpanded = true
                chevron.setImageResource(R.drawable.ic_baseline_chevron_down_24dp)
                descriptionTV.visibility = VISIBLE
            }
        }
    }
}
