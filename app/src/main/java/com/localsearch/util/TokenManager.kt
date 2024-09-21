package com.localsearch.util

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("authToken", Context.MODE_PRIVATE)

    // 액세스 토큰 저장
    fun saveTokens(accessToken: String, refreshToken: String) {
        with(sharedPreferences.edit()) {
            putString("accessToken", accessToken)
            putString("refreshToken", refreshToken)
            apply()  // 비동기적으로 저장
        }
    }

    // 토큰 가져오기
    fun getAccessToken(): String? {
        return sharedPreferences.getString("accessToken", null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refreshToken", null)
    }

    // 토큰 삭제 (로그아웃 시)
    fun clearTokens() {
        with(sharedPreferences.edit()) {
            remove("accessToken")
            remove("refreshToken")
            apply()
        }
    }
}
