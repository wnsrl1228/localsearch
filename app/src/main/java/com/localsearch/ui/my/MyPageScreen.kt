package com.localsearch.ui.my

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.localsearch.LocalSearchTopAppBar
import com.localsearch.R
import com.localsearch.navigation.NavigationDestination
import com.localsearch.ui.auth.AuthDestination
import com.localsearch.ui.components.MenuButton
import com.localsearch.util.TokenManager

object MyPageDestination : NavigationDestination {
    override val route = "mypage"
    override val titleRes = R.string.app_name
}

@Composable
fun MyPageScreen(
    navigateBack: () -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            LocalSearchTopAppBar(
                title = "현지탐색",
                canNavigateBack = true,
                navigateUp = navigateBack,
            )
        }
    ) { innerPadding ->
        MyPageBody(
            tokenManager = TokenManager(context),
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun MyPageBody(
    tokenManager: TokenManager,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    MenuButton(
        text = "로그아웃",
        onClick = {
            // 토큰 삭제
            tokenManager.clearTokens()

            // Auth 페이지로 이동하고 뒤로가기 기록 모두 제거
            navController.navigate(AuthDestination.route) {
                popUpTo(0) { inclusive = true }
            }
        },
        modifier = modifier
    )
}