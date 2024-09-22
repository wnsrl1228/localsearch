package com.localsearch.data.model.search

import com.localsearch.ui.search.PlaceData

data class LocalSearchResponse(
    val places: List<PlaceData> = emptyList()
)

