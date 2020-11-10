package com.example.kotlinyoutube.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.ui.YouTubeMediaPlayer.Companion.WEB_URL_KEY
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.activity_webview.actionBack

class WebViewActivity: AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        // action bar: go back arrow
        actionBack.setOnClickListener {
            finish()
        }

        // display YouTube logo
        val youtubeImageUrl = "https://www.youtube.com/yts/img/marketing/browsers/yt_logo_rgb_light-vflc4oMnY.png"
        Picasso.with(applicationContext).load(youtubeImageUrl).into(youtubeLogo)

        // load web url
        val url = intent.extras?.getString(WEB_URL_KEY)

        val webView = findViewById<WebView>(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        // https://stackoverflow.com/questions/47872078/how-to-load-an-url-inside-a-webview-using-android-kotlin
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
            }
        })
        webView.loadUrl(url)
    }
}
