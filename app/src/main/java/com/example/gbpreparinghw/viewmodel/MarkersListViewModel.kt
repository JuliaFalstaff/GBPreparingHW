package com.example.gbpreparinghw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gbpreparinghw.model.repo.AppState
import com.example.gbpreparinghw.model.repo.ILocalRepository
import com.example.gbpreparinghw.model.room.MarkerEntity
import com.example.gbpreparinghw.usecase.markers.MarkerListInteractor
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent

class MarkersListViewModel(
    private val liveDataForViewToObserve: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {

    private val repositoryImpl : ILocalRepository by KoinJavaComponent.inject(ILocalRepository::class.java)

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.IO
                + SupervisorJob()
    )

    private var job: Job? = null

    fun getData(): LiveData<AppState> {
        getMarkersData()
        return liveDataForViewToObserve
    }

    fun getMarkersData() {
        job?.cancel()
        job = viewModelCoroutineScope.launch {
            liveDataForViewToObserve.postValue(AppState.Success(repositoryImpl.getAllMarkers()))
        }
    }

    fun updateMarker(marker: MarkerEntity){
        viewModelCoroutineScope.launch {
            repositoryImpl.updateMarker(marker)
        }
    }

    override fun onCleared() {
        viewModelCoroutineScope.cancel()
        super.onCleared()
    }
}