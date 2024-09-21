package com.localsearch.data.api.auth

import retrofit2.http.GET
import retrofit2.http.POST

interface SearchService {
    @GET("search")
    suspend fun search()
}