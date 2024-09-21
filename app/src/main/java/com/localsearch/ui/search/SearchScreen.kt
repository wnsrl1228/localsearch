package com.localsearch.ui.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
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
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    // 허용 권한
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    var defaultSpot by remember { mutableStateOf(LatLng(37.497868, 127.028026)) }
    var currentSpot by remember { mutableStateOf(LatLng(37.497868, 127.028026)) }
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

    // 실시간 위치 데이터 TODO: 추후 삭제, currentSpot도 필요없음
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position }
            .collect { position ->
                val currentLatLng = position.target
                currentSpot = LatLng(currentLatLng.latitude, currentLatLng.longitude)
                // 위치 정보를 활용하는 로직을 여기에 추가
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
                title = "Marker in DefaultSpot",
                snippet = "This is the current spot"
            )
        }

        // 버튼과 텍스트를 하단에 배치
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter) // 하단 중앙에 배치
                .padding(16.dp), // 여백 추가
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
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
                }
            ) {
                Text(text = "내 위치로 이동")
            }
            Button(
                onClick = {}
            ) {
                Text(text = "Allow")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${currentSpot.latitude}, ${currentSpot.longitude}") // TODO : 추후 삭제
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