package com.localsearch.ui.place

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.localsearch.LocalSearchTopAppBar
import com.localsearch.R
import com.localsearch.navigation.NavigationDestination
import com.localsearch.ui.search.PlaceData
import com.localsearch.ui.search.SearchBody

object PlaceDetailDestination : NavigationDestination {
    override val route = "place-detail"
    override val titleRes = R.string.app_name
    const val placeData = "placeData"
    val routeWithArgs = "${PlaceDetailDestination.route}/{$placeData}"
}

@Composable
fun PlaceDetailScreen(
    navigateBack: () -> Unit,
    placeData: PlaceData?,
    modifier: Modifier = Modifier
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
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // 이미지 공간
//        Image(
//            painter = , // 이미지 URL 사용
//            contentDescription = "장소 이미지",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .clip(RoundedCornerShape(8.dp)),
//            contentScale = ContentScale.Crop
//        )

        Spacer(modifier = Modifier.height(16.dp))

        // 장소 이름
        Text(
            text = placeData?.displayName ?: "장소 이름",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // 주소
        Text(
            text = "주소 정보 없음",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // 카테고리
        Text(
            text = "카테고리 정보 없음",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // 평점 및 리뷰 개수
        Text(
            text = "평점: 리뷰)",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ReviewItem()
        // 리뷰 리스트
        Column(
            modifier = Modifier.weight(1f),
//            contentPadding = PaddingValues(bottom = 56.dp) // 버튼 공간 확보
        ) {

            ReviewItem()
            ReviewItem()
            ReviewItem()
        }

        // 리뷰 작성하기 버튼
        Button(
            onClick = { /* 리뷰 작성하기 로직 */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "리뷰 작성하기")
        }
    }
}

@Composable
fun ReviewItem() {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Text(
            text = "유저이름",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "코멘트",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "평점: ",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}