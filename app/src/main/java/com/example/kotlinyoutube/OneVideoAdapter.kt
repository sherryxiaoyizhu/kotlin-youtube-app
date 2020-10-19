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
import java.text.NumberFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class OneVideoAdapter(private val onePlaylist: OnePlaylist): RecyclerView.Adapter<OneVideoViewHolder>() {

    companion object {
        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * Companion.SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS
    }

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
        Picasso.with(holder.view.context).load(oneVideo.snippet.thumbnails.standard.url).into(
            thumbnailImageView
        )

        // display video details: views, published time, likes, dislikes
        val viewCount = getThousands(oneVideo.statistics.viewCount)
        val publishedAt = getTimeAgo(stringToDate(oneVideo.snippet.publishedAt.substringBefore('T')))
        holder.view.viewCount_publishedAt_TV.text = "$viewCount views • $publishedAt"
        holder.view.likeCountTV.text = getShortScale(oneVideo.statistics.likeCount)
        holder.view.dislikeCountTV.text = getShortScale(oneVideo.statistics.dislikeCount)
    }

    // convert Integer to String with comma for thousands
    private fun getThousands(count: String): String {
        return NumberFormat.getNumberInstance(Locale.US)
            .format(count.toInt())
    }

    // convert String to Date
    private fun stringToDate(date: String): Date {
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())
    }

    // count occurrences of a given character in a string
    private fun countOccurrences(s: String, c: Char): Int {
        return s.filter{ it == c }.count()
    }

    // convert String number in billions, millions, thousands, e.g., 317,000 -> 317K
    private fun getShortScale(number: String): String {
        val numInThousands = getThousands(number)
        val leadingNum = numInThousands.substringBefore(',')
        val numCommas = countOccurrences(numInThousands, ',')
        return leadingNum + when (numCommas) {
            0 -> ""
            1 -> "K" // thousand
            2 -> "M" // million
            else -> "B" // billion
        }
    }

    // convert published date to ... time ago
    // * Source: https://stackoverflow.com/questions/35858608/how-to-convert-time-to-time-ago-in-android
    private fun currentDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    private fun getTimeAgo(date: Date): String {
        var time = date.time
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = currentDate().time
        if (time > now || time <= 0) {
            return "in the future"
        }

        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> "moments ago"
            diff < 2 * MINUTE_MILLIS -> "a minute ago"
            diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
            diff < 2 * HOUR_MILLIS -> "an hour ago"
            diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
            diff < 48 * HOUR_MILLIS -> "yesterday"
            else -> "${diff / DAY_MILLIS} days ago"
        }
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
