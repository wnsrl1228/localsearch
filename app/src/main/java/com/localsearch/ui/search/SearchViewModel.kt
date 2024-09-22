package com.localsearch.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localsearch.data.api.RetrofitInstance
import com.localsearch.util.parseErrorResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class SearchUiState(
    val localPlaceList: List<PlaceData> = emptyList(),
    val selectedCategory: Category = Category.FOOD,
    val selectedSort: Sort = Sort.POPULARITY,
    val errorMessage: String? = null,
    val isSearchLoading: Boolean = false,
)

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val searchService = RetrofitInstance.searchService;

    fun search(latitude: Double, longitude: Double, radius: Double) {


        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearchLoading = true)

            try {
                val response = searchService.search(latitude, longitude, radius, _uiState.value.selectedCategory.serverValue, _uiState.value.selectedSort.name)

                if (response.isSuccessful) {
                    val body = response.body()

                    _uiState.value = _uiState.value.copy(
                        localPlaceList = body?.places ?: emptyList(),
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = parseErrorResponse(errorBody)
                    _uiState.value = _uiState.value.copy(errorMessage = errorResponse?.message ?: "잘못된 요청입니다.")
                }
            } catch (e: Exception) {
                // 서버 오류 (500대) 또는 기타 예외 처리
                _uiState.value = _uiState.value.copy(errorMessage = "서버와의 연결에 문제가 발생했습니다.")
            } finally {
                _uiState.value = _uiState.value.copy(isSearchLoading = false)
            }
        }
    }

    fun setCategory(category: Category) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }
}


data class PlaceData(
    val id: String = "",
    val displayName: String = "",
    val primaryTypeDisplayName: String = "",
    val formattedAddress: String = "",
    val googleMapsUri: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)

enum class Category(val serverValue: String, val koreaName: String) {
    FOOD("food", "음식점"),
    CAFE_BAKERY("cafeBakery", "카페/빵"),
    ACCOMMODATION("accommodation", "숙박"),
    STORE("store", "상점"),
    MEDICAL("medical", "의료");
}

enum class Sort {
    POPULARITY, DISTANCE
}