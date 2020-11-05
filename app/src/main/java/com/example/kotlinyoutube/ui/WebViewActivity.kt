package com.example.kotlinyoutube.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
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
