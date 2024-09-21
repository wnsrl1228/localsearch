package com.localsearch

import android.app.Application
import android.content.Intent
import com.localsearch.data.api.RetrofitInstance

class LocalSearchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitInstance.initialize(this) {
            navigateToAuthScreen() // 토큰 갱신 실패 시 홈 화면으로 이동
        }
    }

    private fun navigateToAuthScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}