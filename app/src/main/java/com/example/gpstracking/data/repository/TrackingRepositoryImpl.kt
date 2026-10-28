package com.example.gpstracking.data.repository

import com.example.gpstracking.data.local.datastore.SettingsDataStore
import com.example.gpstracking.data.local.db.TripDao
import com.example.gpstracking.data.local.db.TripEntity
import com.example.gpstracking.data.mapper.toEntity
import com.example.gpstracking.data.mapper.toDomain
import com.example.gpstracking.domain.model.LocationPoint
import com.example.gpstracking.domain.model.Settings
import com.example.gpstracking.domain.model.Trip
import com.example.gpstracking.domain.repository.TrackingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrackingRepositoryImpl @Inject constructor(
    private val tripDao: TripDao,
    private val settingsDataStore: SettingsDataStore
) : TrackingRepository {

    override fun getAllTrips(): Flow<List<Trip>> =
        tripDao.getAllTrips().map { list -> list.map { it.toDomain() } }

    override suspend fun startTrip(startTime: Long): Long =
        tripDao.insertTrip(
            TripEntity(startTime = startTime, endTime = null, distanceMeters = 0f, durationMillis = 0L)
        )

    override suspend fun stopTrip(
        tripId: Long,
        endTime: Long,
        distanceMeters: Float,
        durationMillis: Long
    ) = tripDao.finishTrip(tripId, endTime, distanceMeters, durationMillis)

    override suspend fun saveLocationPoint(point: LocationPoint) =
        tripDao.insertLocationPoint(point.toEntity())

    override fun getSettings(): Flow<Settings> = settingsDataStore.settingsFlow

    override suspend fun setUpdateInterval(seconds: Int) =
        settingsDataStore.setUpdateInterval(seconds)

    override suspend fun setBackgroundTracking(enabled: Boolean) =
        settingsDataStore.setBackgroundTracking(enabled)
}
