package com.example.gpstracking.domain.model

data class Trip(
    val id: Long,
    val startTime: Long,
    val endTime: Long?,
    val distanceMeters: Float,
    val durationMillis: Long
)
