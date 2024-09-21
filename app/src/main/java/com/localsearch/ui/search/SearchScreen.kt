package com.localsearch.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.localsearch.LocalSearchTopAppBar
import com.localsearch.R
import com.localsearch.data.api.RetrofitInstance
import com.localsearch.navigation.NavigationDestination
import kotlinx.coroutines.launch

object SearchDestination : NavigationDestination {
    override val route = "search"
    override val titleRes = R.string.app_name
}

@Composable
fun SearchScreen(
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
        SearchBody(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun SearchBody(
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier) {
        Text(text = "Search Page")
        Button(onClick = {
        /*TODO : 임시로 설정 */
            coroutineScope.launch {
                val searchService = RetrofitInstance.searchService
                try {
                    searchService.search()
                } catch (e: Exception) {

                }
            }

        }) {

        }
    }
}