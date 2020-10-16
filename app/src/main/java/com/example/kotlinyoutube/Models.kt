package com.example.kotlinyoutube

// parse Json: MainActivity
class Playlist(val items: List<Video>)

class Video(val snippet: Snippet)

class Snippet(val publishedAt: String, val title: String, val description: String,
              val channelTitle: String, val position: Int, val thumbnails: Thumbnails)

// parse Json: VideoActivity
class PlaylistDetail(val items: List<VideoDetail>)

class VideoDetail(val snippet: SnippetDetail)

class SnippetDetail(val publishedAt: String, val title: String, val description: String,
              val channelTitle: String, val position: Int, val thumbnails: Thumbnails, val statistics: Statistics)

class Statistics(val viewCount: String, val likeCount: String, val dislikeCount: String, val commentCount: String)

// common models
class Thumbnails(val standard: Standard)

class Standard(val url: String)