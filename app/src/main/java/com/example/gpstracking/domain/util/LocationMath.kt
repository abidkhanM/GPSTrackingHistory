package com.example.gpstracking.domain.util

import kotlin.math.*

fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val r = 6_371_000.0
    val dLat = (lat2 - lat1).toRad()
    val dLon = (lon2 - lon1).toRad()
    val a = sin(dLat / 2).pow(2) +
            cos(lat1.toRad()) * cos(lat2.toRad()) * sin(dLon / 2).pow(2)
    return (r * 2 * atan2(sqrt(a), sqrt(1 - a))).toFloat()
}

private fun Double.toRad() = Math.toRadians(this)
