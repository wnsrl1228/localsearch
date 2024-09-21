package com.localsearch.data.api

import android.content.Context
import com.localsearch.data.api.auth.AuthService
import com.localsearch.data.api.auth.SearchService
import com.localsearch.data.network.TokenInterceptor
import com.localsearch.util.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080"
    private lateinit var tokenManager: TokenManager
    private lateinit var tokenInterceptorClient: OkHttpClient

    fun initialize(context: Context, onTokenRefreshFail: () -> Unit) {
        tokenManager = TokenManager(context)

        tokenInterceptorClient = OkHttpClient().newBuilder()
            .addInterceptor(TokenInterceptor(tokenManager, authService, onTokenRefreshFail))
            .build()
    }

    val authService: AuthService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(AuthService::class.java)
    }

    val searchService: SearchService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(tokenInterceptorClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(SearchService::class.java)
    }
}