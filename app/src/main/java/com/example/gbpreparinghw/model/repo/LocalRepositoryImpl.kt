package com.example.gbpreparinghw.model.repo

import com.example.gbpreparinghw.model.room.MarkerDao
import com.example.gbpreparinghw.model.room.MarkerEntity
import com.example.gbpreparinghw.utils.toMarkerOptions
import com.google.android.gms.maps.model.MarkerOptions

class LocalRepositoryImpl(private val markerDao: MarkerDao) : ILocalRepository {

    override suspend fun addMarker(marker: MarkerEntity) {
        markerDao.insert(marker)
    }

    override suspend fun deleteMarker(marker: MarkerEntity) {
        markerDao.delete(marker)
    }

    override suspend fun getAllMarkers(): MutableList<MarkerOptions> {
        return markerDao.getAll().map { it.toMarkerOptions() }.toMutableList()
    }

    override suspend fun updateMarker(marker: MarkerEntity) {
        markerDao.update(marker)
    }

    override suspend fun insertAll(entity: List<MarkerEntity>) {
        markerDao.insertAll(entity)
    }
}