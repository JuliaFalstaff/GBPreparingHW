package com.example.gbpreparinghw.model.room

import androidx.room.*

@Dao
interface MarkerDao {

    @Query("SELECT * FROM MarkerEntity")
    suspend fun getAll(): MutableList<MarkerEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: MarkerEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entity: List<MarkerEntity>)

    @Update
    suspend fun update(entity: MarkerEntity)

    @Delete
    suspend fun delete(entity: MarkerEntity)
}