package com.localsearch

import android.app.Application
import com.localsearch.data.api.RetrofitInstance

class LocalSearchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitInstance.initialize(this)
    }
}