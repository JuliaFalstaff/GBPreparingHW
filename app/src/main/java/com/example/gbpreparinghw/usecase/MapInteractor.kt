package com.example.gbpreparinghw.usecase

import com.example.gbpreparinghw.model.repo.AppState
import com.example.gbpreparinghw.model.repo.ILocalRepository
import com.example.gbpreparinghw.model.room.MarkerEntity

class MapInteractor(private val repositoryImpl : ILocalRepository): IMainInteractor<AppState> {

    override suspend fun addMarker(marker: MarkerEntity) {
        repositoryImpl.addMarker(marker)
    }

    override suspend fun getAllMarkers(): AppState {
        return AppState.Success(repositoryImpl.getAllMarkers())
    }

    override suspend fun addAll(entity: List<MarkerEntity>) {
        repositoryImpl.insertAll(entity)
    }
}