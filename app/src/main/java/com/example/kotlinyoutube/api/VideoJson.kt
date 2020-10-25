package com.example.kotlinyoutube.api

// JSON DATA:

//open class Base{ }

// ** parse Json: MainActivity **

class Playlist(val items: List<Video>)
//class Playlist(val items: List<Video>): Base() {}

class Video(val snippet: Snippet)

class Snippet(val publishedAt: String, val title: String, val description: String,
              val channelTitle: String, val position: Int, val thumbnails: Thumbnails)

// ** parse Json: OneVideoActivity **

class OnePlaylist(val items: List<OneVideo>)
//class OnePlaylist(val items: List<OneVideo>): Base() {}

class OneVideo(val snippet: OneSnippet, val statistics: Statistics)

class OneSnippet(val publishedAt: String, val title: String, val description: String,
                 val channelTitle: String, val position: Int, val thumbnails: Thumbnails)

class Statistics(val viewCount: String, val likeCount: String, val dislikeCount: String, val commentCount: String)

// ** shared models **
class Thumbnails(val standard: Standard)

class Standard(val url: String)
