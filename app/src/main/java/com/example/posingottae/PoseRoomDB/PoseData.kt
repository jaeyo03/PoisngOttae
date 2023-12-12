package com.example.posingottae.PoseRoomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PoseData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "Pose Name" )
    val poseName : String,
    @ColumnInfo(name = "User Left Arm")
    val userLeftArm : Double,
    @ColumnInfo(name = "User Right Arm")
    val userRightArm : Double,
    @ColumnInfo(name = "User Left Leg")
    val userLeftLeg : Double,
    @ColumnInfo(name = "User Right Leg")
    val userRightLeg : Double

)