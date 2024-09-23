package com.localsearch.data.model.search

import com.localsearch.ui.place.ReviewData

data class PlaceReviewResponse(
    val placeId: String = "",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val isReviewEmpty: Boolean = true,
    val reviews: List<ReviewData>
)
