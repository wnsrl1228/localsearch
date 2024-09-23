package com.localsearch.ui.place

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localsearch.data.api.RetrofitInstance
import com.localsearch.ui.search.PlaceData
import com.localsearch.ui.search.Sort
import com.localsearch.util.parseErrorResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlaceDetailUiState(
    val reviewList: List<ReviewData> = emptyList(),
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val isReviewEmpty: Boolean = true,
    val selectedSort: Sort = Sort.POPULARITY,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)


class PlaceDetailViewModel(private val placeData: PlaceData?) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceDetailUiState())
    val uiState: StateFlow<PlaceDetailUiState> = _uiState.asStateFlow()
    private val searchService = RetrofitInstance.searchService

    init {
        println(placeData)
        initialize()
    }

    private fun initialize() {

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                if (placeData == null) return@launch
                val response = searchService.getPlaceReview(placeData.id)

                if (response.isSuccessful) {
                    val body = response.body()
                    println("=sadasd")
                    println(body)
                    _uiState.value = _uiState.value.copy(
                        reviewList = body?.reviews ?: emptyList(),
                        rating = body?.rating ?: 0.0,
                        reviewCount = body?.reviewCount ?: 0,
                        isReviewEmpty = body?.isReviewEmpty ?: true
                    )
                    _uiState.value = _uiState.value.copy(errorMessage = null)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = parseErrorResponse(errorBody)
                    _uiState.value = _uiState.value.copy(errorMessage = errorResponse?.message ?: "잘못된 요청입니다.")
                }
            } catch (e: Exception) {
                // 서버 오류 (500대) 또는 기타 예외 처리
                Log.d("api PlaceDetailViewModel.init", "$e")
                _uiState.value = _uiState.value.copy(errorMessage = "서버와의 연결에 문제가 발생했습니다.")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }

        }
    }
}
data class ReviewData(
    val nickname: String = "",
    val content: String = "",
    val rating: Double = 0.0,
    val createdAt: String = "",
    val updatedAt: String = ""
)