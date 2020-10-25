package com.example.kotlinyoutube

import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class MainViewModel: ViewModel() {

    companion object {
        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS
    }

    // refresh to fetch new videos
    fun repoFetch() {

    }

    // convert Integer to String with comma for thousands
    fun getThousands(count: String): String {
        return NumberFormat.getNumberInstance(Locale.US)
            .format(count.toInt())
    }

    // convert String to Date
    fun stringToDate(date: String): Date {
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())
    }

    // count occurrences of a given character in a string
    fun countOccurrences(s: String, c: Char): Int {
        return s.filter{ it == c }.count()
    }

    // convert String number in billions, millions, thousands, e.g., 317,000 -> 317K
    fun getShortScale(number: String): String {
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
    fun currentDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    fun getTimeAgo(date: Date): String {
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

// JSON DATA:

// parse Json: MainActivity
class Playlist(val items: List<Video>)

class Video(val snippet: Snippet)

class Snippet(val publishedAt: String, val title: String, val description: String,
              val channelTitle: String, val position: Int, val thumbnails: Thumbnails)

// parse Json: OneVideoActivity
class OnePlaylist(val items: List<OneVideo>)

class OneVideo(val snippet: OneSnippet, val statistics: Statistics)

class OneSnippet(val publishedAt: String, val title: String, val description: String,
              val channelTitle: String, val position: Int, val thumbnails: Thumbnails)

class Statistics(val viewCount: String, val likeCount: String, val dislikeCount: String, val commentCount: String)

// shared models
class Thumbnails(val standard: Standard)

class Standard(val url: String)