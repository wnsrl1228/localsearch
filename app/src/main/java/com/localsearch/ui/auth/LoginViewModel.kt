package com.localsearch.ui.auth

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.localsearch.data.api.RetrofitInstance
import com.localsearch.data.model.auth.LoginRequest
import com.localsearch.data.model.auth.SignUpRequest
import com.localsearch.util.TokenManager
import com.localsearch.util.parseErrorResponse
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpSuccess: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = mutableStateOf(LoginUiState())
    val uiState: State<LoginUiState> = _uiState

    private val authService = RetrofitInstance.authService;

    fun login(userId: String, password: String) {

        val message = validateInputs(userId, password);
        if (message.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = message)
            return;
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response = authService.login(LoginRequest(userId, password))

                if (response.isSuccessful) {
                    val loginTokens = response.body()
                    val tokenManager = TokenManager(getApplication<Application>().applicationContext)
                    tokenManager.saveTokens(loginTokens!!.accessToken, loginTokens!!.refreshToken)
                    _uiState.value = _uiState.value.copy(isSignUpSuccess = true)
                } else {

                    val errorBody = response.errorBody()?.string()

                    if (response.code() == 400) {
                        val errorResponse = parseErrorResponse(errorBody)
                        _uiState.value = _uiState.value.copy(errorMessage = errorResponse?.message ?: "잘못된 요청입니다.")
                    } else {
                        handleClientError(response.code())
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

    // 입력 검증 함수
    private fun validateInputs(userId: String, password: String): String {
        return when {
            userId.isBlank() -> "아이디를 입력해주세요."
            userId.length < 5 || userId.length > 30 -> "아이디는 5~30자까지 입력할 수 있습니다."
            password.isBlank() -> "비밀번호를 입력해주세요."
            password.length < 5 || password.length > 30 -> "비밀번호는 5~30자까지 입력할 수 있습니다."
            else -> "" // 모든 검증을 통과하면 빈 문자열 반환
        }
    }

    private fun handleClientError(code: Int) {
        when (code) {
            404 -> _uiState.value = _uiState.value.copy(errorMessage = "잘못된 요청입니다.")
            // 다른 에러 코드 처리
            else -> _uiState.value = _uiState.value.copy(errorMessage = "오류가 발생했습니다")
        }
    }
}