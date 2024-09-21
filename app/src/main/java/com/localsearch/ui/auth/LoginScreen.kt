package com.localsearch.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.localsearch.LocalSearchTopAppBar
import com.localsearch.R
import com.localsearch.navigation.NavigationDestination
import com.localsearch.ui.components.MenuButton
import com.localsearch.ui.components.PasswordInput
import com.localsearch.ui.components.TextInput

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.app_name
}

@Composable
fun LoginScreen(
    navigateBack: () -> Unit,
    navigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            LocalSearchTopAppBar(
                title = "현지탐색",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        LoginBody(
            navigateToSearch = navigateToSearch,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}
@Composable
fun LoginBody(
    viewModel: LoginViewModel = viewModel(),
    navigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (uiState.isSignUpSuccess) {
        // 회원가입 성공 시 새로운 페이지로 이동
        LaunchedEffect(Unit) {
            navigateToSearch()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 입력란을 중앙에 배치
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // 중앙 정렬
            modifier = Modifier.align(Alignment.Center)
        ) {
            // 아이디 입력란
            TextInput(
                value = userId,
                onValueChange = { userId = it },
                label = "아이디"
            )

            // 비밀번호 입력란
            PasswordInput(
                value = password,
                onValueChange = { password = it }
            )

            // 에러 메시지가 있을 때 보여줌
            uiState.errorMessage?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
                )
            }
        }

        // 로그인 버튼을 하단에 배치
        MenuButton(
            onClick = {
                viewModel.login(
                    userId = userId,
                    password = password,
                )
            },
            text = "로그인",
            enabled = !uiState.isLoading, // 로딩 중일 때 버튼 비활성화
            modifier = Modifier
                .align(Alignment.BottomCenter) // 하단 중앙에 위치
                .padding(bottom = 16.dp) // 하단 여백 추가
        )

        // 로딩 중일 때 회전하는 애니메이션 표시
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}