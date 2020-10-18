package com.example.kotlinyoutube

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinyoutube.OneVideoActivity.Companion.videoURL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_one_video.view.*

class OneVideoAdapter(private val onePlaylist: OnePlaylist): RecyclerView.Adapter<OneVideoViewHolder>() {

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneVideoViewHolder {
        val customView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_one_video, parent, false)
        return OneVideoViewHolder(customView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OneVideoViewHolder, position: Int) {
        val oneVideo = onePlaylist.items[0]

        // display video detail thumbnail
        val thumbnailImageView = holder.view.videoDetailImageView
        Picasso.with(holder.view.context).load(oneVideo.snippet.thumbnails.standard.url).into(thumbnailImageView)

        // display video details: views, published time, likes, dislikes
        //val viewCount = oneVideo.snippet.statistics.viewCount
        val publishedAt = oneVideo.snippet.publishedAt
        holder.view.viewCount_publishedAt_TV.text = publishedAt
        //holder.view.likeCountTV.text = oneVideo.snippet.statistics.likeCount
        //holder.view.dislikeCountTV.text = oneVideo.snippet.statistics.dislikeCount
    }
}

class OneVideoViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    companion object {
        const val WEB_URL_KEY = "WEB_URL"
    }

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, WebViewActivity::class.java).apply {
                putExtra(WEB_URL_KEY, videoURL)
            }
            view.context.startActivity(intent)
        }
    }
}
