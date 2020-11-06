package com.example.kotlinyoutube.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.kotlinyoutube.api.OneVideo
import com.example.kotlinyoutube.api.Repository
import com.example.kotlinyoutube.api.Video
import com.example.kotlinyoutube.api.YouTubeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    // initialization
    private val api = YouTubeApi.create()
    private val repository = Repository(api)

    private var title = MutableLiveData<String>()
    private var searchTerm = MutableLiveData<String>()
    private var video = MutableLiveData<String>().apply {
        value = "Home"
    }

    private val favoriteVideos = MutableLiveData<List<Video>>().apply {
        value = mutableListOf()
    }

    private var netVideos = MediatorLiveData<List<Video>>().apply {
        // addSource to enable swipe-refresh feature
        addSource(video) {
            viewModelScope.launch(
                context = viewModelScope.coroutineContext
                        + Dispatchers.IO
            ) {
                postValue(repository.getPlaylist())
            }
        }
    }

    private var searchVideos = MediatorLiveData<List<Video>>().apply {
        addSource(netVideos) { netVideos ->
            value = netVideos.filter {
                it.searchFor(searchTerm.value ?: "")
            }
        }
        addSource(searchTerm) { searchTerm ->
            value = netVideos.value?.filter {
                it.searchFor(searchTerm)
            }
        }
    }

    private var searchFavorites = MediatorLiveData<List<Video>>().apply {
        addSource(favoriteVideos) { favoriteVideos ->
            value = favoriteVideos.filter {
                it.searchFor(searchTerm.value ?: "")
            }
        }
    }

    init {
        searchFavorites.apply {
            addSource(searchTerm) { searchTerm ->
                value = favoriteVideos.value?.filter {
                    it.searchFor(searchTerm)
                }
            }
        }
    }

    // swipe refresh to fetch new videos for the playlist
    fun repoFetch() {
        val fetch = video.value
        video.value = fetch
    }

    fun setSearchTerm(term: String) {
        searchTerm.value = term
    }

    fun setTitle(newTitle: String) {
        title.value = newTitle
    }

    fun setTitleToHome() {
        title.value = "KotlinYouTube"
    }

    fun observeTitle(): LiveData<String> {
        return title
    }

    fun observeVideos(): LiveData<List<Video>> {
        return searchVideos
    }

    fun observeFavorites(): LiveData<List<Video>> {
        return searchFavorites
    }

    fun isFavorite(video: Video): Boolean {
        return favoriteVideos.value?.contains(video)!!
    }

    fun addFavorite(video: Video) {
        val favList = favoriteVideos.value?.toMutableList()
        if (!favList?.contains(video)!!) {
            favList.add(video)
            favoriteVideos.value = favList
        }
    }

    fun removeFavorite(video: Video) {
        val favList = favoriteVideos.value?.toMutableList()
        if (favList?.contains(video)!!) {
            favList.remove(video)
            favoriteVideos.value = favList
        }
    }

    // *** ***

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
