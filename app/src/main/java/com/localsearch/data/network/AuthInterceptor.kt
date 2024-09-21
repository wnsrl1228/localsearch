package com.localsearch.data.network

import com.localsearch.data.api.auth.AuthService
import com.localsearch.data.model.auth.AccessTokenRequest
import com.localsearch.util.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor(
    private val tokenManager: TokenManager,
    private val authService: AuthService,
    private val onTokenRefreshFail: () -> Unit
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = tokenManager.getAccessToken()

        // 1. accessToken 이 없을 경우 재로그인
        if (accessToken == null) {
            tokenManager.clearTokens();
            onTokenRefreshFail() // 토큰 갱신 실패 처리 콜백 호출
            return chain.proceed(originalRequest) // 기본 요청으로 처리
        }

        // 2. accessToken 헤더에 부착
        val requestWithToken = addAuthorizationHeader(originalRequest, accessToken)
        var response = chain.proceed(requestWithToken)

        // 401 응답 시 토큰 갱신 시도
        if (response.code() == 401) {
            response.close() // 응답 닫기
            response = try {
                handleTokenRefresh(chain, originalRequest)
            } catch (e: Exception) {
                tokenManager.clearTokens()
                onTokenRefreshFail() // 토큰 갱신 실패 처리 콜백 호출
                chain.proceed(originalRequest) // 기본 요청으로 처리
            }
        }

        return response
    }

    private fun addAuthorizationHeader(request: Request, accessToken: String?): Request {
        return request.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
    }

    private fun handleTokenRefresh(chain: Interceptor.Chain, originalRequest: Request): Response {
        val refreshToken = tokenManager.getRefreshToken()

        // 피프레쉬 토큰이 없는 경우 재로그인
        if (refreshToken == null) {
            tokenManager.clearTokens();
            throw Exception("Token refresh failed, please log in again.")
        }

        // 리프레시 토큰으로 새로운 액세스 토큰 요청
        return runBlocking {
            val newAccessTokenResponse = authService.refreshAccessToken(AccessTokenRequest(refreshToken))

            if (newAccessTokenResponse.isSuccessful) {
                val newAccessToken = newAccessTokenResponse.body()?.accessToken
                tokenManager.saveTokens(newAccessToken!!, refreshToken) // 새 토큰 저장

                // 원래 요청을 새로운 액세스 토큰으로 재요청
                val newRequestWithToken = addAuthorizationHeader(originalRequest, newAccessToken)
                chain.proceed(newRequestWithToken)
            } else {
                tokenManager.clearTokens()
                throw Exception("Token refresh failed, please log in again.")
            }
        }
    }
}