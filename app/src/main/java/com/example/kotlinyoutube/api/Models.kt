package com.example.kotlinyoutube.api

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.text.clearSpans

// JSON DATA:

// ******* parse Json: MainActivity *******

class Playlist(val items: List<Video>) //

data class Video(val snippet: Snippet) {

    companion object {
        private fun setSpan(fulltext: SpannableString, subtext: String): Boolean {
            if (subtext.isEmpty()) return true
            val i = fulltext.indexOf(subtext, ignoreCase = true)
            if (i == -1) return false
            fulltext.setSpan(
                ForegroundColorSpan(Color.BLUE), i, i + subtext.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return true
        }
    }

    private fun removeAllCurrentSpans(){
        // Erase all spans
        snippet.title.clearSpans()
    }

    fun searchFor(searchTerm: String): Boolean {
        // XXX Write me, search both regular posts and subreddit listings
        removeAllCurrentSpans()

        // search for titles
        return setSpan(snippet.title, searchTerm)
    }

    // We want videos fetched at two different times to compare as equal.
    // By default, they will be different objects with different hash codes.
    override fun equals(other: Any?) : Boolean =
        if (other is Video) {
            snippet == other.snippet
        } else {
            false
        }
}

class Snippet(val publishedAt: String, val title: SpannableString, val description: String,
              val channelTitle: String, val position: Int, val thumbnails: Thumbnails)

// ******* parse Json: OneVideoActivity *******

class OnePlaylist(val items: List<OneVideo>)

class OneVideo(val snippet: OneSnippet, val statistics: Statistics)

class OneSnippet(val publishedAt: String, val title: String, val description: String,
                 val channelTitle: String, val position: Int, val thumbnails: Thumbnails)

class Statistics(val viewCount: String, val likeCount: String, val dislikeCount: String, val commentCount: String)

// ******* shared models *******

class Thumbnails(val standard: Standard)

class Standard(val url: String)
