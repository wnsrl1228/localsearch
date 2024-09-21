package com.localsearch.util

import com.google.gson.Gson
import com.localsearch.data.model.ErrorResponse

// 에러 응답을 파싱하는 함수
fun parseErrorResponse(jsonString: String?): ErrorResponse? {
    return jsonString?.let {
        val gson = Gson()
        gson.fromJson(it, ErrorResponse::class.java)
    }
}