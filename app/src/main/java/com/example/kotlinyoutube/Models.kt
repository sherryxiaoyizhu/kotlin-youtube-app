package com.example.kotlinyoutube

// parse Json
class Playlist(val items: List<Video>)

class Video(val snippet: Snippet)

class Snippet(val publishedAt: String, val title: String, val description: String,
              val channelTitle: String, val position: Int, val thumbnails: Thumbnails)

class Thumbnails(val standard: Standard)

class Standard(val url: String)