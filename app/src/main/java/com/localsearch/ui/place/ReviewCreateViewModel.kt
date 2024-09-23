package com.localsearch.ui.place

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localsearch.data.api.RetrofitInstance
import com.localsearch.data.model.search.ReviewCreateRequest
import com.localsearch.util.parseErrorResponse
import kotlinx.coroutines.launch


data class ReviewCreateUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreateSuccess: Boolean = false
)

class ReviewCreateViewModel: ViewModel()  {

    private val _uiState = mutableStateOf(ReviewCreateUiState())
    val uiState: State<ReviewCreateUiState> = _uiState

    private val authService = RetrofitInstance.searchService;

    fun create(placeId: String?, content: String?, rating: Double) {

        if (content.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "리뷰을 입력해주세요.")
            return;
        }
        if (placeId.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "잘못된 접근입니다.")
            return;
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response = authService.createReview(ReviewCreateRequest(placeId, content, rating))

                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(isCreateSuccess = true)
                } else {

                    val errorBody = response.errorBody()?.string()

                    if (response.code() == 400) {
                        val errorResponse = parseErrorResponse(errorBody)
                        _uiState.value = _uiState.value.copy(errorMessage = errorResponse?.message ?: "잘못된 요청입니다.")
                    }
                }
            } catch (e: Exception) {
                // 서버 오류 (500대) 또는 기타 예외 처리
                _uiState.value = _uiState.value.copy(errorMessage = "서버와의 연결에 문제가 발생했습니다.")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}