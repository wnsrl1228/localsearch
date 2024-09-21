package com.localsearch.data.model.auth

data class LoginRequest(
    val userId: String,
    val password: String
)