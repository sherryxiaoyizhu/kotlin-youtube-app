package com.example.kotlinyoutube

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.video_row.view.*

class MainAdapter(val playlist: Playlist): RecyclerView.Adapter<CustomViewHolder>() {

    private val numVideos = 10

    override fun getItemCount(): Int {
        return numVideos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.video_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val video = playlist.items.get(position)

        // display thumbnail
        val thumbnailImageView = holder.view.videoImageView
        Picasso.with(holder.view.context).load(video.snippet.thumbnails.standard.url).into(thumbnailImageView)

        // display text
        holder.view.videoTitleTV.text = video.snippet.title
        holder.view.channelNameTV.text = video.snippet.channelTitle+" • "+"20K views\n4 days ago"
    }
}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}