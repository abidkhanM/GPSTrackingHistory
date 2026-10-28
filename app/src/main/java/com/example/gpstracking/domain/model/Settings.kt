package com.example.gpstracking.domain.model

data class Settings(
    val updateIntervalSeconds: Int = DEFAULT_INTERVAL,
    val backgroundTracking: Boolean = false
) {
    companion object {
        const val DEFAULT_INTERVAL = 5
    }
}
