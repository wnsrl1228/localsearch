package com.localsearch.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}
@Composable
fun LoginBody(
    modifier: Modifier = Modifier
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
        }

        // 로그인 버튼을 하단에 배치
        MenuButton(
            onClick = {
                // 로그인 처리 로직 추가
            },
            text = "로그인",
            modifier = Modifier
                .align(Alignment.BottomCenter) // 하단 중앙에 위치
                .padding(bottom = 16.dp) // 하단 여백 추가
        )
    }
}