package com.example.gpstracking.domain.usecase

import com.example.gpstracking.domain.model.LocationPoint
import com.example.gpstracking.domain.repository.TrackingRepository
import javax.inject.Inject

class SaveLocationPointUseCase @Inject constructor(
    private val repository: TrackingRepository
) {
    suspend operator fun invoke(point: LocationPoint) = repository.saveLocationPoint(point)
}
