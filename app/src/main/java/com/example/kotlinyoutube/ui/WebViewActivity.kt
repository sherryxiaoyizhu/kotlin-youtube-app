package com.example.kotlinyoutube.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.ui.OneVideoAdapter.Companion.WEB_URL_KEY
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

        webview.settings.javaScriptEnabled = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.useWideViewPort = true

        // load web url
        val url = intent.extras?.getString(WEB_URL_KEY)
        webview.loadUrl(url)
    }
}

//class WebViewActivity: Fragment() {
//
//    companion object {
//        fun newInstance(): WebViewActivity {
//            return WebViewActivity()
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // inflate fragment recycler view
//        val root = inflater.inflate(R.layout.activity_webview, container, false)
//
//        webview.settings.javaScriptEnabled = true
//        webview.settings.loadWithOverviewMode = true
//        webview.settings.useWideViewPort = true
//
//        parentFragmentManager.apply {
//            if (backStackEntryCount == 0) { // // only enables actionFavorite in home fragment
//                beginTransaction()
//                    .add(R.id.main_frame, newWebFrag)
//                    .addToBackStack("...")
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                    .commit()
//            }
//        }
//
//        // load web url
//        //val url = intent.extras?.getString(WEB_URL_KEY)
//        println("XXX: WEB_URL_KEY "+WEB_URL_KEY.toString())
//        webview.loadUrl(WEB_URL_KEY)
//
//        // back button
//        requireActivity().onBackPressedDispatcher
//            .addCallback(this) {
//                parentFragmentManager.popBackStack()
//            }
//        return root
//
//    }
//}
