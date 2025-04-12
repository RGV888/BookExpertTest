package com.pp.bookxpert.network

import com.pp.bookxpert.models.ApiResponseItem
import retrofit2.http.GET

interface ApiService {
    @GET("objects")
    suspend fun fetchProducts(): List<ApiResponseItem>
}