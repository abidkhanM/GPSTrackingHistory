package com.example.gpstracking.domain.usecase

import com.example.gpstracking.domain.model.Trip
import com.example.gpstracking.domain.repository.TrackingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTripsUseCase @Inject constructor(
    private val repository: TrackingRepository
) {
    operator fun invoke(): Flow<List<Trip>> = repository.getAllTrips()
}
