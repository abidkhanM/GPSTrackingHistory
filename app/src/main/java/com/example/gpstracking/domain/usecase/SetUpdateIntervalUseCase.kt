package com.example.gpstracking.domain.usecase

import com.example.gpstracking.domain.repository.TrackingRepository
import javax.inject.Inject

class SetUpdateIntervalUseCase @Inject constructor(
    private val repository: TrackingRepository
) {
    suspend operator fun invoke(seconds: Int) = repository.setUpdateInterval(seconds)
}
