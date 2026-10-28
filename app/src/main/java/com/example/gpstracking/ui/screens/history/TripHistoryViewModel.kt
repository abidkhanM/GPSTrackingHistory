package com.example.gpstracking.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gpstracking.domain.model.Trip
import com.example.gpstracking.domain.usecase.GetAllTripsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class TripHistoryUiState(
    val trips: List<Trip> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class TripHistoryViewModel @Inject constructor(
    getAllTripsUseCase: GetAllTripsUseCase
) : ViewModel() {

    val uiState: StateFlow<TripHistoryUiState> = getAllTripsUseCase()
        .map { trips -> TripHistoryUiState(trips = trips, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TripHistoryUiState()
        )
}
