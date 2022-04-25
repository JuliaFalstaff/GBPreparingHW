package com.example.gbpreparinghw.model.repo

import com.example.gbpreparinghw.model.room.MarkerEntity
import com.google.android.gms.maps.model.MarkerOptions

interface ILocalRepository {
    suspend fun addMarker(marker: MarkerEntity)

    suspend fun deleteMarker(marker: MarkerEntity)

    suspend fun getAllMarkers(): MutableList<MarkerOptions>

    suspend fun updateMarker(marker: MarkerEntity)

    suspend fun insertAll(entity: List<MarkerEntity>)
}