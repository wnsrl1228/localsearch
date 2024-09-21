package com.localsearch.data.model.auth

data class SignUpRequest(
    val userId: String,
    val password: String,
    val nickname: String,
)
