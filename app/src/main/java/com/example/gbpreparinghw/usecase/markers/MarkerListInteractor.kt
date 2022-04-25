package com.example.gbpreparinghw.usecase.markers

import com.example.gbpreparinghw.model.repo.AppState
import com.example.gbpreparinghw.model.repo.ILocalRepository
import com.example.gbpreparinghw.model.room.MarkerEntity

class MarkerListInteractor(private val repositoryImpl : ILocalRepository): IMarkerListInteractor<AppState> {

    override suspend fun getMarkersData(): AppState {
        TODO("Not yet implemented")
    }

    override suspend fun updateMarker(marker: MarkerEntity) {
        TODO("Not yet implemented")
    }
}