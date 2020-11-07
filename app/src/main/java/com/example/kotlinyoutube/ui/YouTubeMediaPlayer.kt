package com.example.kotlinyoutube.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinyoutube.MainActivity.Companion.MY_SECRET_API_KEY
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.ui.HomeAdapter.VH.Companion.VIDEO_ID_KEY
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import kotlinx.android.synthetic.main.activity_webview.*

class YouTubeMediaPlayer: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_youtube_player)
        // set up tool bar
        setSupportActionBar(toolbar)
        // display ActionBar back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val youtubeFrag = supportFragmentManager.findFragmentById(R.id.youtube_fragment)
                as YouTubePlayerFragment

        Log.d("XXX", "YouTubePlayer onCreate...")

        youtubeFrag.initialize(MY_SECRET_API_KEY, object:YouTubePlayer.OnInitializedListener {
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
                    player.cueVideo(VIDEO_ID_KEY)
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
    }
}
