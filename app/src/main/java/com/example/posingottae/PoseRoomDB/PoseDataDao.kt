package com.example.posingottae.PoseRoomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PoseDataDao {
    @Query("SELECT * FROM PoseData")
    fun getAll(): List<PoseData>

    @Insert
    fun insert(lotto: PoseData)

    @Delete
    fun delete(lotto: PoseData)

    @Query("DELETE FROM PoseData")
    fun deleteAll()

}