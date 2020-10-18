package com.example.kotlinyoutube

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_main.view.*

class MainAdapter(val playlist: Playlist): RecyclerView.Adapter<CustomViewHolder>() {

    private val numVideos = 20

    override fun getItemCount(): Int {
        return numVideos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val cellForRow = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_main, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val video = playlist.items[position] // position vs. adapterPosition
        val channelProfileImagePath =
            "https://yt3.ggpht.com/a/AATXAJyljAWnvqx5Lmdp3UP8Js0rSqQLmf3bt76mAnL-=s900-c-k-c0xffffffff-no-rj-mo"

        // display thumbnail
        val thumbnailImageView = holder.view.videoImageView
        Picasso.with(holder.view.context).load(video.snippet.thumbnails.standard.url).into(thumbnailImageView)

        // display channel profile image
        val channelProfileImageView = holder.view.channelProfileImage
        Picasso.with(holder.view.context).load(channelProfileImagePath).into(channelProfileImageView)

        // display text
        holder.view.videoTitleTV.text = video.snippet.title
        holder.view.channelNameTV.text = video.snippet.channelTitle+" • "+"20K views\n4 days ago"

        // to pass video details through intent
        holder.video = video
    }
}

class CustomViewHolder(val view: View, var video: Video? = null): RecyclerView.ViewHolder(view) {

    companion object {
        const val VIDEO_TITLE_KEY = "VIDEO_TITLE"
        const val VIDEO_ID_KEY = "VIDEO_ID"
    }

    init {
        view.setOnClickListener {
            // pass data through intent
            val intent = Intent(view.context, OneVideoActivity::class.java).apply {
                putExtra(VIDEO_TITLE_KEY, video?.snippet?.title)
                val startIdx = "https://i.ytimg.com/vi/".length
                putExtra(VIDEO_ID_KEY, video?.snippet?.thumbnails?.standard?.url?.substring(startIdx))
            }
            view.context.startActivity(intent)
        }
    }
}