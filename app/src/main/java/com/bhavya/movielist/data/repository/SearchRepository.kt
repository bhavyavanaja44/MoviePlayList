package com.bhavya.movielist.data.repository

import com.bhavya.movielist.data.model.Movie
import com.bhavya.movielist.data.network.ApiRequest
import com.bhavya.movielist.data.network.ApiService

class SearchRepository(
    private val api: ApiService
) : ApiRequest() {

    suspend fun getMovies(
        searchTitle: String,
        apiKey: String,
        pageIndex: Int
    ): Movie {

        return apiRequest { api.getSearchResult(searchTitle, apiKey, pageIndex) }
    }


}