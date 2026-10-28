package com.example.gpstracking.domain.usecase

import com.example.gpstracking.domain.repository.TrackingRepository
import javax.inject.Inject

class SetBackgroundTrackingUseCase @Inject constructor(
    private val repository: TrackingRepository
) {
    suspend operator fun invoke(enabled: Boolean) = repository.setBackgroundTracking(enabled)
}
