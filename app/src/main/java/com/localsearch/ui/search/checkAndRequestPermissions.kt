package com.localsearch.ui.search

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

fun checkAndRequestPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    callback: (Double, Double) -> Unit
) {

    /** 권한이 이미 있는 경우 **/
    if (permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }) {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val long = location.longitude
                    callback(lat, long)
                }
            }
            .addOnFailureListener { exception ->
                // Handle location retrieval failure
                exception.printStackTrace()
            }
        Log.d("good", "권한이 이미 존재합니다.")
    }
    /** 권한이 없는 경우 **/
    else {
        launcher.launch(permissions)
        Log.d("no", "권한을 요청하였습니다.")
    }
}

fun checkAndRequestPermissions(
    context: Context,
    permissions: Array<String>,
    callback: (Double, Double) -> Unit
) {

    /** 권한이 이미 있는 경우 **/
    if (permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }) {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val long = location.longitude
                    callback(lat, long)
                }
            }
            .addOnFailureListener { exception ->
                // Handle location retrieval failure
                exception.printStackTrace()
            }
        Log.d("good", "권한이 이미 존재합니다.")
    }
}