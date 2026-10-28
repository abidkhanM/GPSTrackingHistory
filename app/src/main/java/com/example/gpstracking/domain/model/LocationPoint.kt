package com.example.gpstracking.domain.model

data class LocationPoint(
    val id: Long = 0,
    val tripId: Long,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val speed: Float
)
