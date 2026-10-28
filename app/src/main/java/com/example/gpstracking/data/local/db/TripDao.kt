package com.example.gpstracking.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert
    suspend fun insertTrip(trip: TripEntity): Long

    @Insert
    suspend fun insertLocationPoint(point: LocationPointEntity)

    @Query("UPDATE trips SET endTime = :endTime, distanceMeters = :distanceMeters, durationMillis = :durationMillis WHERE id = :tripId")
    suspend fun finishTrip(tripId: Long, endTime: Long, distanceMeters: Float, durationMillis: Long)

    @Query("SELECT * FROM trips ORDER BY startTime DESC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM location_points WHERE tripId = :tripId ORDER BY timestamp ASC")
    fun getPointsForTrip(tripId: Long): Flow<List<LocationPointEntity>>
}
