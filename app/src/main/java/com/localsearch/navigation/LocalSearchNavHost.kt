package com.localsearch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.localsearch.ui.search.SearchDestination
import com.localsearch.ui.search.SearchScreen
import com.localsearch.util.TokenManager

@Composable
fun LocalSearchNavHost(
    navController: NavHostController,
    tokenManager: TokenManager,
    modifier: Modifier = Modifier,
) {
//    tokenManager.clearTokens()
    val isLoggedIn = tokenManager.getAccessToken() != null
    val startDestination = if (isLoggedIn) {
        SearchDestination.route// 로그인 후 이동할 화면 (예: 메인 화면)
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
                navigateToSearch = {navController.navigate(SearchDestination.route)}
            )
        }

        composable(route = SearchDestination.route) {
            SearchScreen(
                navigateBack = {navController.popBackStack()},
            )
        }

    }
}
