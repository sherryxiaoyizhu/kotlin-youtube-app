package com.example.kotlinyoutube.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.example.kotlinyoutube.MainActivity.Companion.CHANNEL_PROFILE_IMAGE
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.api.Video
import com.google.common.collect.Iterators.getNext
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_youtube_player.*
import kotlinx.android.synthetic.main.row_main.view.*

class HomeAdapter(private val viewModel: MainViewModel)
    : RecyclerView.Adapter<HomeAdapter.VH>() {

    private var videoList: List<Video> = listOf()

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val customView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_main, parent, false)
        return VH(customView, viewModel, videoList)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(videoList[holder.adapterPosition])
    }

    // set up view holder
    class VH(val view: View, private val viewModel: MainViewModel,
             val videoList: List<Video>, var oneVideo: Video? = null, var nextVideo: Video? = null)
        : RecyclerView.ViewHolder(view) {

        companion object {
            const val VIDEO_TITLE_KEY = "VIDEO_TITLE"
            const val VIDEO_ID_KEY = "VIDEO_ID"
            const val NEXT_VIDEO_URL_KEY = "NEXT_VIDEO_URL"
            const val NEXT_VIDEO_TITLE_KEY = "NEXT_VIDEO_TITLE"
            const val NEXT_DISPLAY_TEXT_KEY = "NEXT_DISPLAY_TEXT"
        }

        init {
            // pass data from main view to one video view through intent
            view.videoImageView.setOnClickListener {
                val intent = Intent(view.context, YouTubeMediaPlayer::class.java).apply {
                    val startIdx = "https://i.ytimg.com/vi/".length
                    val title = oneVideo?.snippet?.title.toString()
                    val videoId = oneVideo?.snippet?.thumbnails?.high?.url?.substring(startIdx)

                    val nextVideoThumbnailUrl = nextVideo?.snippet?.thumbnails?.high?.url
                    val nextVideoTitle = nextVideo?.snippet?.title.toString()
                    val publishedDate = nextVideo?.snippet?.publishedAt?.substringBefore('T')
                    val displayTime = viewModel.getTimeAgo(viewModel.stringToDate(publishedDate!!))
                    val displayText = nextVideo?.snippet?.channelTitle + " • $displayTime"

                    putExtra(VIDEO_TITLE_KEY, title)
                    putExtra(VIDEO_ID_KEY, videoId)
                    putExtra(NEXT_VIDEO_URL_KEY, nextVideoThumbnailUrl)
                    putExtra(NEXT_VIDEO_TITLE_KEY, nextVideoTitle)
                    putExtra(NEXT_DISPLAY_TEXT_KEY, displayText)
                }
                view.context.startActivity(intent)
            }

            // enable favorite feature in home fragment
            view.favBut.setOnClickListener {
                val item = oneVideo!!
                if (viewModel.isFavorite(item)) {
                    viewModel.removeFavorite(item)
                    itemView.favBut.setImageResource(R.drawable.ic_favorite_border_black_24dp)

                } else {
                    viewModel.addFavorite(item)
                    itemView.favBut.setImageResource(R.drawable.ic_favorite_black_24dp)
                }
            }
        }

        fun bind(item: Video) {
            // fetch data
            val channelProfileImagePath = CHANNEL_PROFILE_IMAGE
            val title = item.snippet.title
            val thumbnailUrl = item.snippet.thumbnails.high.url
            val publishedDate = item.snippet.publishedAt.substringBefore('T')
            val displayTime = viewModel.getTimeAgo(viewModel.stringToDate(publishedDate))
            val displayText = item.snippet.channelTitle + " • $displayTime"

            // get next video
            val idx = videoList.indexOf(item)
            val nextItem =  videoList[(idx+1)%videoList.size]

            // display thumbnail
            val thumbnailImageView = view.videoImageView
            Picasso.with(view.context).load(thumbnailUrl).into(thumbnailImageView)

            // display channel profile image
            val channelProfileImageView = view.channelProfileImage
            Picasso.with(view.context).load(channelProfileImagePath).into(channelProfileImageView)

            // display text
            view.videoTitleTV.text = title
            view.displayTV.text = displayText

            // to pass video details through intent
            oneVideo = item
            nextVideo = nextItem

            // favorite button in home fragment
            if (viewModel.isFavorite(item)) {
                itemView.favBut.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                itemView.favBut.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
        }
    }

    fun submitList(list: List<Video>) {
        videoList = list
    }
}
