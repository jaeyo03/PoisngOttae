package com.example.posingottae.PoseRoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PoseData::class], version = 1)
abstract class PoseDB : RoomDatabase() {
    abstract fun poseDataDao(): PoseDataDao

    companion object{
        private var INSTANCE: PoseDB? = null

        fun getInstance(context: Context) :PoseDB{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PoseDB::class.java,
                    "Pose_Database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
