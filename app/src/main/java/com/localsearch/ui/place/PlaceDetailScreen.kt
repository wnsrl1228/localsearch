package com.localsearch.ui.place

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.localsearch.LocalSearchTopAppBar
import com.localsearch.R
import com.localsearch.navigation.NavigationDestination
import com.localsearch.ui.components.MenuButton
import com.localsearch.ui.search.PlaceData

object PlaceDetailDestination : NavigationDestination {
    override val route = "place-detail"
    override val titleRes = R.string.app_name
    const val placeData = "placeData"
    val routeWithArgs = "${route}/{$placeData}"
}

@Composable
fun PlaceDetailScreen(
    navigateBack: () -> Unit,
    placeData: PlaceData?,
) {
    val viewModel: PlaceDetailViewModel = viewModel(factory = PlaceDetailViewModelFactory(placeData))

    Scaffold(
        topBar = {
            LocalSearchTopAppBar(
                title = "현지탐색",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        PlaceDetailBody(
            viewModel = viewModel,
            placeData = placeData,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun PlaceDetailBody(
    viewModel: PlaceDetailViewModel,
    placeData: PlaceData?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // 장소 이름
        Text(
            text = placeData?.displayName ?: "장소 이름",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 4.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(placeData?.googleMapsUri))
                    context.startActivity(intent)
                }
        )

        // 주소
        Text(
            text = placeData?.formattedAddress ?: "주소 정보 없음",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // 카테고리
        Text(
            text = placeData?.primaryTypeDisplayName ?: "카테고리 정보 없음",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // 평점 및 리뷰 개수
        Text(
            text = "평점: ${viewModel.uiState.value.rating}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "리뷰 ${viewModel.uiState.value.reviewCount}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 리뷰 리스트
        LazyColumn(
            modifier = Modifier.weight(1f),
//            contentPadding = PaddingValues(bottom = 56.dp) // 버튼 공간 확보
        ) {

            items(viewModel.uiState.value.reviewList) { review ->
                ReviewItem(
                    nickname = review.nickname,
                    content = review.content,
                    rating = review.rating,
                    createAt = review.createdAt
                )
            }
        }

        // 리뷰 작성하기 버튼
        MenuButton(
            text = "리뷰 작성하기",
            onClick = { /* 리뷰 작성하기 로직 */ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ReviewItem(
    nickname: String = "",
    content: String = "",
    rating: Double = 0.0,
    createAt: String = "",
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Text(
            text = nickname,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "$rating",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = createAt,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}