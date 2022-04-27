package com.example.gbpreparinghw.usecase.markers

import com.example.gbpreparinghw.model.room.MarkerEntity

interface IMarkerListInteractor<AppState> {
    suspend fun getMarkersData(): AppState
    suspend fun updateMarker(marker: MarkerEntity)
}