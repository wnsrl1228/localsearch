package com.localsearch.data.network

import com.localsearch.util.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val tokenManager: TokenManager?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 액세스 토큰을 헤더에 추가
        val accessToken = tokenManager?.getAccessToken()

        val requestWithToken = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(requestWithToken)
    }
}