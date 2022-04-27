package com.example.gbpreparinghw.model.repo

import com.google.android.gms.maps.model.MarkerOptions

sealed class AppState{
    data class Success(val markersData: MutableList<MarkerOptions>) : AppState()
    data class Error(val error : Throwable) : AppState()
    object Loading : AppState()
}
