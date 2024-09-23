package com.localsearch.ui.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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

object ReviewCreateDestination : NavigationDestination {
    override val route = "review-create"
    override val titleRes = R.string.app_name
    const val placeId = "placeId"
    val routeWithArgs = "${route}/{$placeId}"
}

@Composable
fun ReviewCreateScreen(
    navigateBack: () -> Unit,
    placeId: String? = "",
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
        ReviewCreateBody(
            placeId = placeId,
            navigateBack = navigateBack,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun ReviewCreateBody(
    viewModel: ReviewCreateViewModel = viewModel(),
    placeId: String?,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState
    var content by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0.0) }

    if (uiState.isCreateSuccess) {
        // 회원가입 성공 시 새로운 페이지로 이동
        LaunchedEffect(Unit) {
            navigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "리뷰 작성", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("리뷰 내용") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("평점")
        RatingBar(
            rating = rating,
            onRatingChanged = { rating = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 에러 메시지가 있을 때 보여줌
        uiState.errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
            )
        }

        MenuButton(
            text = "리뷰 등록",
            onClick = {
                viewModel.create(
                    placeId = placeId,
                    content = content,
                     rating = rating
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
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

@Composable
fun RatingBar(
    rating: Double,
    onRatingChanged: (Double) -> Unit
) {
    Row(horizontalArrangement = Arrangement.Center) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.Add,
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onRatingChanged(i.toDouble()) }
            )
        }
    }
}