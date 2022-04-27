package com.example.gbpreparinghw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gbpreparinghw.model.repo.AppState
import com.example.gbpreparinghw.model.repo.ILocalRepository
import com.example.gbpreparinghw.model.repo.LocalRepositoryImpl
import com.example.gbpreparinghw.model.room.MarkerDao
import com.example.gbpreparinghw.model.room.MarkerEntity
import com.example.gbpreparinghw.usecase.MapInteractor
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent

class MapsViewModel(private val liveDataForViewToObserve: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {

    private val repositoryImpl : ILocalRepository by KoinJavaComponent.inject(ILocalRepository::class.java)

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.IO
                + SupervisorJob())

    private var job: Job? = null

    fun getData() : LiveData<AppState> {
        return liveDataForViewToObserve
    }

    fun addMarker(marker : MarkerEntity){
        viewModelCoroutineScope.launch {
            repositoryImpl.addMarker(marker)
        }
    }

    fun getAllMarkers() {
        liveDataForViewToObserve.value = AppState.Loading
        job?.cancel()
        job = viewModelCoroutineScope.launch {
            liveDataForViewToObserve.postValue(AppState.Success(repositoryImpl.getAllMarkers()))
        }
    }

    fun addAll(entity: List<MarkerEntity>) {
        viewModelCoroutineScope.launch {
            repositoryImpl.insertAll(entity)
        }
    }

    override fun onCleared() {
        viewModelCoroutineScope.cancel()
        super.onCleared()
    }
}