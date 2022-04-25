package com.example.gbpreparinghw.utils

import com.example.gbpreparinghw.model.room.MarkerEntity
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


fun Marker.toEntity(): MarkerEntity {
    return MarkerEntity(
        id = 0,
        title = this.title!!,
        snippet = this.snippet,
        lat = this.position.latitude,
        lng = this.position.longitude
    )
}

fun MarkerEntity.toMarkerOptions() : MarkerOptions {
    return MarkerOptions()
        .title(this.title)
        .snippet(this.snippet)
        .position(
            LatLng(this.lat, this.lng)
        )
}

fun MarkerOptions.toEntity() : MarkerEntity {
    return MarkerEntity(
        id = 0,
        title = this.title!!,
        snippet = this.snippet!!,
        lat = this.position.latitude,
        lng = this.position.longitude
    )
}

