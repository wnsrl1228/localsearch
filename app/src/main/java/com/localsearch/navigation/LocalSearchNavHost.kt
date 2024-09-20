package com.localsearch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.localsearch.ui.auth.AuthDestination
import com.localsearch.ui.auth.AuthScreen
import com.localsearch.ui.auth.LoginDestination
import com.localsearch.ui.auth.LoginScreen
import com.localsearch.ui.auth.SignUpDestination
import com.localsearch.ui.auth.SignUpScreen

@Composable
fun LocalSearchNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val isLoggedIn = false // 로그인 상태를 확인하는 함수 TODO : 로그인 구현 후 변경
    val startDestination = if (isLoggedIn) {
        "main_screen" // 로그인 후 이동할 화면 (예: 메인 화면)
    } else {
        AuthDestination.route // 로그인 페이지
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(route = AuthDestination.route) {
            AuthScreen(
                navigateToLoginScreen = {navController.navigate(LoginDestination.route)},
                navigateToSignUpScreen = {navController.navigate(SignUpDestination.route)},
            )
        }

        composable(route = LoginDestination.route) {
            LoginScreen(
                navigateBack = {navController.popBackStack()},
            )
        }

        composable(route = SignUpDestination.route) {
            SignUpScreen(
                navigateBack = {navController.popBackStack()},
            )
        }
    }
}
