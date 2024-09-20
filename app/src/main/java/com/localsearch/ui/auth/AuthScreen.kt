package com.localsearch.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.localsearch.R
import com.localsearch.navigation.NavigationDestination
import com.localsearch.ui.components.MenuButton

object AuthDestination : NavigationDestination {
    override val route = "auth"
    override val titleRes = R.string.app_name
}

@Composable
fun AuthScreen(
    navigateToLoginScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        AuthBody(
            onLoginButtonClick = navigateToLoginScreen,
            onSignUpButtonClick = navigateToSignUpScreen,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun AuthBody(
    onLoginButtonClick: () -> Unit,
    onSignUpButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 로고를 중앙에 배치
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "logo",
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center) // 중앙에 위치
        )

        // 버튼들을 하단에 배치
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
            modifier = Modifier
                .align(Alignment.BottomCenter) // 하단 중앙에 위치
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium)) // 하단 여백 추가
        ) {
            MenuButton(
                onClick = onLoginButtonClick,
                text = "로그인"
            )

            MenuButton(
                onClick = onSignUpButtonClick,
                text = "회원가입"
            )
        }
    }
}


