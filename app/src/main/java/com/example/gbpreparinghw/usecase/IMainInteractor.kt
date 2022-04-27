package com.example.gbpreparinghw.usecase

import com.example.gbpreparinghw.model.room.MarkerEntity

interface IMainInteractor<AppState> {
    suspend fun addMarker(marker : MarkerEntity)
    suspend fun getAllMarkers(): AppState
    suspend fun addAll(entity: List<MarkerEntity>)
}