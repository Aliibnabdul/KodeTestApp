package com.example.koder.data.network

import retrofit2.http.GET

interface ApiInterface {
    @GET("users")
    suspend fun getEmployees(): ApiResponseModel
}