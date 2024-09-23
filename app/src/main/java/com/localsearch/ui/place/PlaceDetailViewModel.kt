package com.localsearch.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.localsearch.ui.search.PlaceData
import kotlinx.coroutines.launch

class PlaceDetailViewModel(private val placeData: PlaceData?) : ViewModel() {

    init {
        initialize()
    }

    fun initialize() {

        viewModelScope.launch {


        }
    }
}