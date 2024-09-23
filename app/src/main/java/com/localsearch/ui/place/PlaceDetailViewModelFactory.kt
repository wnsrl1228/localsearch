package com.localsearch.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.localsearch.ui.search.PlaceData

class PlaceDetailViewModelFactory(private val placeData: PlaceData?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceDetailViewModel::class.java)) {
            return PlaceDetailViewModel(placeData) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}