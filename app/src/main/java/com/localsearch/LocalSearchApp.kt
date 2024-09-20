package com.localsearch

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.localsearch.navigation.LocalSearchNavHost


@Composable
fun LocalSearchApp(navController: NavHostController = rememberNavController()) {
    LocalSearchNavHost(navController = navController)
}
