package com.localsearch.data.api.search

import com.localsearch.data.model.search.LocalSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchService {
    @GET("search")
    suspend fun search(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double,
        @Query("category") category: String,
        @Query("sort") rankPreference: String = "POPULARITY"
    ): Response<LocalSearchResponse>
}