package com.example.kotlinyoutube.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.ui.OneVideoActivity.Companion.videoUrl
import com.example.kotlinyoutube.api.OnePlaylist
import com.example.kotlinyoutube.api.OneVideo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_one_video.view.*

class OneVideoAdapter(private val onePlaylist: OnePlaylist,
                      private val viewModel: MainViewModel)
    : RecyclerView.Adapter<OneVideoAdapter.VH>() {

    companion object {
        const val WEB_URL_KEY = "WEB_URL"
    }

    override fun getItemCount(): Int {
        return 1 // fetch one video
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val customView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_one_video, parent, false)
        return VH(customView, viewModel)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val oneVideo = onePlaylist.items[holder.adapterPosition]
        holder.bind(oneVideo)
    }

    inner class VH(val view: View, val viewModel: MainViewModel): RecyclerView.ViewHolder(view) {

        init {
            view.videoDetailImageView.setOnClickListener {
                val intent = Intent(view.context, WebViewActivity::class.java).apply {
                    putExtra(WEB_URL_KEY, videoUrl)
                }
                view.context.startActivity(intent)
            }

            // enable favorite feature in favorites (and main..) fragment
            itemView.rowFav.setOnClickListener {
                val item =onePlaylist.items[adapterPosition]
                if (viewModel.isFavorite(item)) {
                    viewModel.removeFavorite(item)
                    itemView.rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)

                } else {
                    viewModel.addFavorite(item)
                    itemView.rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
                }
            }
        }

        fun bind(item: OneVideo) {
            // fetch data
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

            // display video details: views, published time, number of likes, comments, description
            val views = viewModel.getThousands(viewCount)
            val time = viewModel.getTimeAgo(viewModel.stringToDate(publishedDate))
            view.viewCount_publishedAt_TV.text = "$views views • $time"
            view.likeCountTV.text = viewModel.getShortScale(likesCount)
            view.commentsCount.text = viewModel.getShortScale(commentsCount)
            view.oneVideoDescriptionTV.text = description

            // favorite button in one video fragment
            if (viewModel.isFavorite(item)) {
                itemView.rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                itemView.rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
        }
    }
}
