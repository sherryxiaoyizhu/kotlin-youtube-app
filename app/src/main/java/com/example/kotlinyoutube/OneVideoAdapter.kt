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

class OneVideoAdapter(private val onePlaylist: OnePlaylist, private val viewModel: MainViewModel)
    : RecyclerView.Adapter<OneVideoViewHolder>() {

    override fun getItemCount(): Int {
        return 1 // return one video
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneVideoViewHolder {
        val customView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_one_video, parent, false)
        return OneVideoViewHolder(customView, viewModel)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OneVideoViewHolder, position: Int) {
        val oneVideo = onePlaylist.items[holder.adapterPosition]
        holder.bind(oneVideo)
    }
}

class OneVideoViewHolder(val view: View, private val viewModel: MainViewModel): RecyclerView.ViewHolder(view) {

    companion object {
        const val WEB_URL_KEY = "WEB_URL"
    }

    init {
        view.videoDetailImageView.setOnClickListener {
            val intent = Intent(view.context, WebViewActivity::class.java).apply {
                putExtra(WEB_URL_KEY, videoURL)
            }
            view.context.startActivity(intent)
        }
    }

    fun bind(item: OneVideo) {
        // get models
        val videoUrl = item.snippet.thumbnails.standard.url
        val viewCount = item.statistics.viewCount
        val publishedDate = item.snippet.publishedAt.substringBefore('T')
        val likesCount = item.statistics.likeCount
        val commentsCount = item.statistics.commentCount
        val description = item.snippet.description

        // display video detail thumbnail
        val thumbnailImageView = view.videoDetailImageView
        Picasso.with(view.context).load(videoUrl).into(
            thumbnailImageView
        )

        // display video details: views, published time, likes, dislikes
        val views = viewModel.getThousands(viewCount)
        val time = viewModel.getTimeAgo(viewModel.stringToDate(publishedDate))
        view.viewCount_publishedAt_TV.text = "$views views • $time"
        view.likeCountTV.text = viewModel.getShortScale(likesCount)
        view.commentsCount.text = viewModel.getShortScale(commentsCount)
        view.oneVideoDescriptionTV.text = description
    }
}