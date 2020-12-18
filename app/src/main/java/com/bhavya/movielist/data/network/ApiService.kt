package com.bhavya.movielist.data.network

import com.bhavya.movielist.data.model.Movie
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
interface ApiService {

    @GET("?type=movie")
    suspend fun getSearchResult(
        @Query(value = "s") searchTitle: String, @Query(value = "apiKey") apiKey: String, @Query(
            value = "page"
        ) pageIndex: Int
    ): Response<Movie>


    companion object {
        operator fun invoke(
            networkInterceptor: NetworkInterceptor
        ): ApiService {

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkInterceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        private const val baseUrl = "https://www.omdbapi.com/"
    }
}