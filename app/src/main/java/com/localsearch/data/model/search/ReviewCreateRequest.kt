package com.localsearch.data.model.search

data class ReviewCreateRequest(
    val placeId: String,
    val content: String,
    val rating: Double,
)
