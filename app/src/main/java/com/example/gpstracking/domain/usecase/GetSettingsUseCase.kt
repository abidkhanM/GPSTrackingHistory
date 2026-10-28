package com.example.gpstracking.domain.usecase

import com.example.gpstracking.domain.model.Settings
import com.example.gpstracking.domain.repository.TrackingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: TrackingRepository
) {
    operator fun invoke(): Flow<Settings> = repository.getSettings()
}
