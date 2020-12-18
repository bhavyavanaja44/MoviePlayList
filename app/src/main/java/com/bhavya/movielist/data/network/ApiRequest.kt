package com.bhavya.movielist.data.network

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class ApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("Error"))
                } catch (e: JSONException) {
                }
            }
            throw Exception(message.toString())
        }
    }

}
