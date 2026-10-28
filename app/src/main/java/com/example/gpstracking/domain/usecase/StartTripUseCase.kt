package com.example.gpstracking.domain.usecase

import com.example.gpstracking.domain.repository.TrackingRepository
import javax.inject.Inject

class StartTripUseCase @Inject constructor(
    private val repository: TrackingRepository
) {
    suspend operator fun invoke(startTime: Long): Long = repository.startTrip(startTime)
}
