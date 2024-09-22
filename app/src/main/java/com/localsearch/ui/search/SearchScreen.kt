package com.localsearch.ui.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.localsearch.LocalSearchTopAppBar
import com.localsearch.R
import com.localsearch.navigation.NavigationDestination
import com.localsearch.ui.components.CategoryButton
import com.localsearch.ui.components.MenuButton
import com.localsearch.ui.components.SmallTextButton

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
                canNavigateBack = false,
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
    viewModel: SearchViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    // 허용 권한
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    var defaultSpot by remember { mutableStateOf(LatLng(37.497868, 127.028026)) }
    var currentSpot by remember { mutableStateOf(LatLng(37.497868, 127.028026)) }
    var currentZoom by remember { mutableStateOf(17f) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultSpot, 17f)
    }
    var mapProperties by remember { mutableStateOf(
        MapProperties(
            minZoomPreference = 7f,
            isMyLocationEnabled = false,  // !!주위 내 위치 사용 즉 권한허용
        )
    ) }

    // Create a permission launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        /** 권한 요청시 동의 했을 경우 **/
        if (areGranted) {
            checkAndRequestPermissions(context, permissions,) { lat, long ->
                defaultSpot = LatLng(lat, long)
            }
        }
    }

    // 실시간 위치 데이터 TODO: 주변 탐색하기 위한 자표값으로 필요함
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position }
            .collect { position ->
                // 위치 정보를 활용하는 로직을 여기에 추가
                val currentLatLng = position.target
                currentSpot = LatLng(currentLatLng.latitude, currentLatLng.longitude)

                currentZoom = position.zoom
            }
    }

    // 현재 위치 변동시 이동
    LaunchedEffect(defaultSpot) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(defaultSpot, 17f),
            durationMs = 2000 // 애니메이션 지속 시간 (1000ms = 1초)
        )
    }

    // 최초 렌더링 시 권한 체크
    LaunchedEffect(true) {
        checkAndRequestPermissions(context, permissions, requestPermissionLauncher) { lat, long ->
            defaultSpot = LatLng(lat, long)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
        ) {
            Marker(
                state = MarkerState(position = defaultSpot),
                title = "내 위치",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            )

            // 장소 리스트에서 마커 추가
            uiState.localPlaceList.forEach { place ->
                Marker(
                    state = MarkerState(position = LatLng(place.latitude, place.longitude)),
                    title = place.displayName,
                    snippet = place.formattedAddress,
                )
            }
        }

        // 카테고리
        Column(modifier = Modifier
            .align(Alignment.TopCenter) // 상단 중앙에 배치
            .padding(top = 70.dp, // 탑바의 높이에 맞춰 여백 추가
                    start = 16.dp,
                    end = 16.dp
            )
        ) {
            // 카테고리 선택 버튼
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // 버튼 간격
            ) {
                items(Category.values().toList()) { category ->
                    CategoryButton(
                        text = category.koreaName,
                        onClick = {
                            viewModel.setCategory(category)
                            viewModel.search(currentSpot.latitude, currentSpot.longitude, getRadiusByZoom(currentZoom))
                                  },
                        modifier = Modifier.weight(1f),
                        isSelected =  uiState.selectedCategory.serverValue != category.serverValue
                    )
                }
            }
        }

        // 버튼과 텍스트를 하단에 배치
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter) // 하단 중앙에 배치
                .padding(16.dp), // 여백 추가
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 에러 메시지가 있을 때 보여줌
            uiState.errorMessage?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
                )
            }
            SmallTextButton(
                onClick = {
                    // 권한 다시 확인,
                    checkAndRequestPermissions(context, permissions, requestPermissionLauncher) { lat, long ->
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(lat, long), 17f)
                    }
                    // 권한 거부인 경우, 권한 설정창으로 이동
                    if (permissions.any {
                            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
                        }) {
                        showDialog = true
                    }
                },
                text = "내 위치로 이동" ,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            MenuButton(
                onClick = { viewModel.search(currentSpot.latitude, currentSpot.longitude, getRadiusByZoom(currentZoom)) },
                text = "주변 탐색하기",
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
//            Text(text = "${currentSpot.latitude}, ${currentSpot.longitude}, ${currentZoom}\"") // TODO : 추후 삭제
//            Text(text = "${currentZoom}\"") // TODO : 추후 삭제
        }
    }

    // 로딩 중일 때 회전하는 애니메이션 표시
    if (uiState.isSearchLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (showDialog) {
        LocationPermissionDialog(
            onRequestPermission = {
                // 앱 설정 창 이동
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun LocationPermissionDialog(
    onRequestPermission: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("위치 권한 요청") },
        text = { Text("위치 서비스를 사용하려면 권한을 허용해야 합니다.") },
        confirmButton = {
            Button(
                onClick = {
                    onRequestPermission()
                    onDismiss()
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}