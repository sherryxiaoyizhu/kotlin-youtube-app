package com.example.kotlinyoutube

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_one_video.view.*

class VideoAdapter(val onePlaylist: OnePlaylist): RecyclerView.Adapter<VideoDetailViewHolder>() {

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val customView = layoutInflater.inflate(R.layout.row_one_video, parent, false)
        return VideoDetailViewHolder(customView)
    }

    override fun onBindViewHolder(holder: VideoDetailViewHolder, position: Int) {
        val oneVideo = onePlaylist.items[0]

        // display video detail thumbnail
        val thumbnailImageView = holder.view.videoDetailImageView
        Picasso.with(holder.view.context).load(oneVideo.snippet.thumbnails.standard.url).into(thumbnailImageView)
    }
}

class VideoDetailViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}
