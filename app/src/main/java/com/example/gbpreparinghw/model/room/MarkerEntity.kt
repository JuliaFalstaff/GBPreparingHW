package com.example.gbpreparinghw.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    val title: String,
    val snippet: String?,
    val lat: Double,
    val lng: Double
)