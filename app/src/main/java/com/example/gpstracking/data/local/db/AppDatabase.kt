package com.example.gpstracking.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TripEntity::class, LocationPointEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}
