package com.example.gpstracking.domain.repository

import com.example.gpstracking.domain.model.LocationPoint
import com.example.gpstracking.domain.model.Settings
import com.example.gpstracking.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface TrackingRepository {
    fun getAllTrips(): Flow<List<Trip>>
    suspend fun startTrip(startTime: Long): Long
    suspend fun stopTrip(tripId: Long, endTime: Long, distanceMeters: Float, durationMillis: Long)
    suspend fun saveLocationPoint(point: LocationPoint)
    fun getSettings(): Flow<Settings>
    suspend fun setUpdateInterval(seconds: Int)
    suspend fun setBackgroundTracking(enabled: Boolean)
}
