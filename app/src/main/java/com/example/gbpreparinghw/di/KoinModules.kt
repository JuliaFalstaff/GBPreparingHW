package com.example.gbpreparinghw.di

import androidx.room.Room
import com.example.gbpreparinghw.model.repo.ILocalRepository
import com.example.gbpreparinghw.model.repo.LocalRepositoryImpl
import com.example.gbpreparinghw.model.room.MapDataBase
import com.example.gbpreparinghw.viewmodel.MapsViewModel
import com.example.gbpreparinghw.viewmodel.MarkersListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(androidContext(), MapDataBase::class.java, "database.db")
            .fallbackToDestructiveMigration().build()
    }
    single { get<MapDataBase>().markerDao() }
    single<ILocalRepository> { LocalRepositoryImpl(markerDao = get()) }
    viewModel { MarkersListViewModel() }
    viewModel { MapsViewModel() }
}