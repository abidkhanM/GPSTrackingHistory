package com.example.gpstracking.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gpstracking.domain.model.Settings
import com.example.gpstracking.domain.usecase.GetSettingsUseCase
import com.example.gpstracking.domain.usecase.SetBackgroundTrackingUseCase
import com.example.gpstracking.domain.usecase.SetUpdateIntervalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val setUpdateIntervalUseCase: SetUpdateIntervalUseCase,
    private val setBackgroundTrackingUseCase: SetBackgroundTrackingUseCase
) : ViewModel() {

    val uiState: StateFlow<Settings> = getSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Settings()
        )

    fun setUpdateInterval(seconds: Int) {
        viewModelScope.launch { setUpdateIntervalUseCase(seconds) }
    }

    fun setBackgroundTracking(enabled: Boolean) {
        viewModelScope.launch { setBackgroundTrackingUseCase(enabled) }
    }
}
