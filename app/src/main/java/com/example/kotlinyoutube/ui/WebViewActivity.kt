package com.example.kotlinyoutube.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.ui.OneVideoAdapter.Companion.WEB_URL_KEY
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.content_webview.*

class WebViewActivity: AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        // set up tool bar
        setSupportActionBar(toolbar)
        // display ActionBar back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        webview.settings.javaScriptEnabled = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.useWideViewPort = true

        // https://stackoverflow.com/questions/7746409/android-webview-launches-browser-when-calling-loadurl
        webview.setWebViewClient(WebViewClient())

        // load web url
        val url = intent.extras?.getString(WEB_URL_KEY)
        webview.loadUrl(url)
    }
}
