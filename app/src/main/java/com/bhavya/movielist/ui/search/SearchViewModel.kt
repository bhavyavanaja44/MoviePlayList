package com.bhavya.movielist.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhavya.movielist.data.model.Movie
import com.bhavya.movielist.data.repository.SearchRepository
import com.bhavya.movielist.util.Constants
import com.bhavya.movielist.util.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel( private val repository: SearchRepository) : ViewModel() {


    private var pageIndex = 0
    private var totalMovies = 0
    private var movieList = ArrayList<Movie.SearchItem?>()

    private val _moviesLiveData = MutableLiveData<ViewState<ArrayList<Movie.SearchItem?>>>()
    val moviesLiveData: LiveData<ViewState<ArrayList<Movie.SearchItem?>>>
    get() = _moviesLiveData

    private val _movieNameLiveData = MutableLiveData<String>()
    val movieNameLiveData: LiveData<String>
    get() = _movieNameLiveData

    private val _loadMoreListLiveData = MutableLiveData<Boolean>()
    val loadMoreListLiveData: LiveData<Boolean>
    get() = _loadMoreListLiveData

    private lateinit var movieResponse: Movie

    init {
        _loadMoreListLiveData.value = false
        _movieNameLiveData.value = ""
    }

    fun getMovies() {
        if (pageIndex == 1) {
            movieList.clear()
            _moviesLiveData.postValue(ViewState.loading())
        } else {
            if (movieList.isNotEmpty() && movieList.last() == null)
                movieList.removeAt(movieList.size - 1)
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (_movieNameLiveData.value != null && _movieNameLiveData.value!!.isNotEmpty()) {
                try {
                    movieResponse = repository.getMovies(
                        _movieNameLiveData.value!!,
                        Constants.API_KEY,
                        pageIndex
                    )
                    withContext(Dispatchers.Main) {
                        if (movieResponse.response == Constants.SUCCESS) {
                            movieList.addAll(movieResponse.search)
                            totalMovies = movieResponse.totalResults.toInt()
                            _moviesLiveData.postValue(ViewState.success(movieList))
                            _loadMoreListLiveData.value = false
                        } else
                            _moviesLiveData.postValue(ViewState.error(movieResponse.error))
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _moviesLiveData.postValue(ViewState.error(e.message!!))
                        _loadMoreListLiveData.value = false
                    }
                }
            }

        }
    }

    fun searchMovie(query: String) {
        _movieNameLiveData.value = query
        pageIndex = 1
        totalMovies = 0
        getMovies()
    }

    fun loadMore() {
        pageIndex++
        getMovies()
    }

    fun loadMoreItems(
        visibleItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int
    ) {
        if (!_loadMoreListLiveData.value!! && (totalItemCount < totalMovies)) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                _loadMoreListLiveData.value = true
            }
        }


    }

}