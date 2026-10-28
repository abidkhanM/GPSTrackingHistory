package com.example.gpstracking.data.mapper

import com.example.gpstracking.data.local.db.LocationPointEntity
import com.example.gpstracking.data.local.db.TripEntity
import com.example.gpstracking.domain.model.LocationPoint
import com.example.gpstracking.domain.model.Trip

fun TripEntity.toDomain(): Trip = Trip(
    id = id,
    startTime = startTime,
    endTime = endTime,
    distanceMeters = distanceMeters,
    durationMillis = durationMillis
)

fun Trip.toEntity(): TripEntity = TripEntity(
    id = id,
    startTime = startTime,
    endTime = endTime,
    distanceMeters = distanceMeters,
    durationMillis = durationMillis
)

fun LocationPoint.toEntity(): LocationPointEntity = LocationPointEntity(
    id = id,
    tripId = tripId,
    timestamp = timestamp,
    latitude = latitude,
    longitude = longitude,
    speed = speed
)
