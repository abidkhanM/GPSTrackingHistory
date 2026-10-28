package com.example.gpstracking.ui.screens.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gpstracking.domain.model.LocationPoint
import com.example.gpstracking.domain.usecase.SaveLocationPointUseCase
import com.example.gpstracking.domain.usecase.StartTripUseCase
import com.example.gpstracking.domain.usecase.StopTripUseCase
import com.example.gpstracking.domain.util.haversineDistance
import com.example.gpstracking.location.LocationDataSource
import com.example.gpstracking.location.LocationUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TrackingState {
    data object Idle : TrackingState
    data object Tracking : TrackingState
    data object Paused : TrackingState
    data object Finished : TrackingState
}

data class TrackingUiState(
    val trackingState: TrackingState = TrackingState.Idle,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val speed: Float = 0f,
    val distanceMeters: Float = 0f,
    val elapsedMillis: Long = 0L,
    val userMessage: String? = null
)

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val startTripUseCase: StartTripUseCase,
    private val stopTripUseCase: StopTripUseCase,
    private val saveLocationPointUseCase: SaveLocationPointUseCase,
    private val locationDataSource: LocationDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackingUiState())
    val uiState: StateFlow<TrackingUiState> = _uiState.asStateFlow()

    private var currentTripId: Long? = null
    private var startTime: Long = 0L
    private var locationJob: Job? = null
    private var lastLat: Double? = null
    private var lastLng: Double? = null

    fun startTracking() {
        viewModelScope.launch {
            startTime = System.currentTimeMillis()
            val tripId = startTripUseCase(startTime)
            currentTripId = tripId
            lastLat = null
            lastLng = null
            _uiState.update { it.copy(
                trackingState = TrackingState.Tracking,
                speed = 0f,
                distanceMeters = 0f,
                elapsedMillis = 0L,
                userMessage = "Tracking started"
            )}
            startLocationUpdates(tripId)
        }
    }

    fun pauseTracking() {
        locationJob?.cancel()
        locationJob = null
        _uiState.update { it.copy(trackingState = TrackingState.Paused) }
    }

    fun resumeTracking() {
        val tripId = currentTripId ?: return
        _uiState.update { it.copy(trackingState = TrackingState.Tracking) }
        startLocationUpdates(tripId)
    }

    fun stopTracking() {
        locationJob?.cancel()
        locationJob = null
        viewModelScope.launch {
            val tripId = currentTripId ?: return@launch
            val state = _uiState.value
            stopTripUseCase(
                tripId = tripId,
                endTime = System.currentTimeMillis(),
                distanceMeters = state.distanceMeters,
                durationMillis = state.elapsedMillis
            )
            _uiState.update { it.copy(
                trackingState = TrackingState.Finished,
                userMessage = "Trip saved"
            )}
        }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    private fun startLocationUpdates(tripId: Long) {
        locationJob = viewModelScope.launch {
            locationDataSource.locationUpdates(intervalMillis = 5_000L)
                .collect { update -> processUpdate(tripId, update) }
        }
    }

    private suspend fun processUpdate(tripId: Long, update: LocationUpdate) {
        val now = System.currentTimeMillis()
        saveLocationPointUseCase(
            LocationPoint(
                tripId = tripId,
                timestamp = now,
                latitude = update.latitude,
                longitude = update.longitude,
                speed = update.speed
            )
        )
        val added = if (lastLat != null && lastLng != null) {
            haversineDistance(lastLat!!, lastLng!!, update.latitude, update.longitude)
        } else 0f

        lastLat = update.latitude
        lastLng = update.longitude

        _uiState.update { it.copy(
            latitude = update.latitude,
            longitude = update.longitude,
            speed = update.speed,
            distanceMeters = it.distanceMeters + added,
            elapsedMillis = now - startTime
        )}
    }
}
