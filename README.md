# MoviePlayList
A simple  movie play list android app using ODMB Search API.

## Features
● Do search a movie name and render results in List View
● Select movies from the results to add to a playlist
● Confirm the list of movies they have selected in a Alert dialog 

## Architecture - MVVM
data: All the data accessing and manipulating components.
ui: View classes along with their corresponding ViewModel.
util: Utility classes.
test: ViewMdel unit Test

## Tech stack and Library used
Kotlin - Official programming language for Android development.
Modern Android Architecture Components - https://developer.android.com/topic/libraries/architecture
  -LiveData 
  -ViewModel 
  -ViewBinding 
Coroutines - clean, simplified asynchronous code that keeps your app responsive while managing long-running tasks such as network calls.
Retrofit - A type-safe HTTP client for Android and Java.
OkHttp3 - For implementing interceptor
Glide - For Image Loading
GSON - A Serialization/deserialization library to convert Java Objects into JSON and back.
Koin - Dependency Injection Framework

### App screenshots
<img src="https://github.com/bhavyavanaja44/MoviePlayList/blob/master/screenshots/search.png" height="600" width="300" hspace="40"><img src="https://github.com/bhavyavanaja44/MoviePlayList/blob/master/screenshots/selected.png" height="600" width="300" hspace="40">

<img src="https://github.com/bhavyavanaja44/MoviePlayList/blob/master/screenshots/welcme_no_network.png" height="600" width="300" hspace="40"><img src="https://github.com/bhavyavanaja44/MoviePlayList/blob/master/screenshots/welcome.png" height="600" width="300" hspace="40">

<img src="https://github.com/bhavyavanaja44/MoviePlayList/blob/master/screenshots/no_movie.png.png" height="600" width="300" hspace="40">

