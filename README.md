# KotlinYouTube

KotlinYouTube is an Android mobile application that renders hit music videos by parsing JSON through YouTube Data API.

### Features & Functionalities

#### Home Page

- Fetch videos from the playlist, display thumbnail image, video title, channel profile image, number of views, and published time from the network through YouTube Data API
- Swipe to refresh the video playlist
- Users to enter search text, filter videos in the playlist, and highlight the search terms
- Click on favorite button in the video list to add videos to favorite list
- Click on favorite button (upper right corner) to see real-time favorite list
- Click on the video thumbnail to redirect to one video view

#### Video Detail Page

- Fetch and display video title (ellipsize in the action bar), thumbnail image, number views, published time, number of likes/ dislikes, number of comments from the network through YouTube Data API
- Show video description in ScrollView
- Click on the video thumbnail and redirect to a WebView

### API

- [YouTube Data API](https://developers.google.com/youtube/v3/getting-started) - basic concepts of YouTube and of the API itself
- [Top 50 This Week & Top 100 Songs 2021](https://www.youtube.com/playlist?list=PLx0sYbCqOb8TBPRdmBHs5Iftvv9TPboYG) - Best New Music Hits Playlist

### Data 

- [JSON](https://www.json.org/json-en.html) - JavaScript Object Notation is an open standard file format
- [Json Parser Online](http://json.parser.online.fr/) - featuring tree view and syntax highlighting
   
### Technology

#### Platform

- [Android Studio](https://developer.android.com/studio?hl=es) - the official integrated development environment for Google's Android operating system

#### Language 

- [Kotlin](https://kotlinlang.org/) - a cross-platform, statically typed, general-purpose programming language with type inference

#### Libraries

- [Retrofit](https://square.github.io/retrofit/) - turns HTTP API into a Java interface
- [OkHttp](https://square.github.io/okhttp/) - an HTTP client for Android and Java applications
- [Gson](https://guides.codepath.com/android/leveraging-the-gson-library) - provides a powerful framework for converting between JSON strings and Java objects
- [Picasso](https://square.github.io/picasso/) - allows for hassle-free image loading in application
- [CircleImageView](https://github.com/hdodenhof/CircleImageView) - a fast circular ImageView perfect for profile images

### Source:

- [CS 395T Android Programming](https://www.cs.utexas.edu/users/witchel/371M/schedule.html) by UT Austin
- [YouTube Tutorial](https://www.youtube.com/playlist?list=PL0dzCUj1L5JGfHj1lwxOq67zAJV3e1S9S) by Lets Build That App Channel