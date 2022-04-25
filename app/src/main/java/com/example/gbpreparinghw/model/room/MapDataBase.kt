package com.example.gbpreparinghw.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MarkerEntity::class], version = 1, exportSchema = false)
abstract class MapDataBase : RoomDatabase() {
    abstract fun markerDao(): MarkerDao
}