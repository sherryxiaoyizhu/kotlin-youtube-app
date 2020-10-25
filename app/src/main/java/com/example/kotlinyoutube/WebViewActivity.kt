package com.example.kotlinyoutube

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.content_webview.*

class WebViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        // set up tool bar
        setSupportActionBar(toolbar)
        // display ActionBar back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // load web url
        val url = intent.extras?.getString(OneVideoViewHolder.WEB_URL_KEY)
        webview.loadUrl(url)
    }
}
