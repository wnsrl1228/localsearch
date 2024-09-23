package com.localsearch.data.api.search

import com.localsearch.data.model.search.LocalSearchResponse
import com.localsearch.data.model.search.PlaceReviewResponse
import com.localsearch.data.model.search.ReviewCreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("search/place/{placeId}")
    suspend fun getPlaceReview(
        @Path("placeId") placeId: String
    ): Response<PlaceReviewResponse>

    @POST("search/review")
    suspend fun createReview(@Body reviewCreateRequest: ReviewCreateRequest): Response<Void>

}