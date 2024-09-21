package com.localsearch.data.api.auth

import com.localsearch.data.model.auth.AccessTokenRequest
import com.localsearch.data.model.auth.LoginRequest
import com.localsearch.data.model.auth.LoginTokens
import com.localsearch.data.model.auth.SignUpRequest
import com.localsearch.data.model.auth.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginTokens>

    @POST("sign-up")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<LoginTokens>

    @POST("auth/token")
    suspend fun refreshAccessToken(@Body accessTokenRequest: AccessTokenRequest): Response<TokenResponse>
}