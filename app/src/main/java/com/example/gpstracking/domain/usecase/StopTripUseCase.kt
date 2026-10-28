package com.example.gpstracking.domain.usecase

import com.example.gpstracking.domain.repository.TrackingRepository
import javax.inject.Inject

class StopTripUseCase @Inject constructor(
    private val repository: TrackingRepository
) {
    suspend operator fun invoke(
        tripId: Long,
        endTime: Long,
        distanceMeters: Float,
        durationMillis: Long
    ) = repository.stopTrip(tripId, endTime, distanceMeters, durationMillis)
}
