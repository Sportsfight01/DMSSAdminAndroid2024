package com.dmss.dmssadminmaintanance.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.xcelsheet.Constants
@Entity(tableName = "assign_task_to_person")
data class AssignTaskToPersonEntityData(
    val name:String,
    val date:String,
    @PrimaryKey(autoGenerate = true) val id: Int? = null

)
@Entity(tableName = "pantry_table")
    data class PantryEntityData(
        val coffee_machine:String,
        val water_dispenser:String,
        val cupboards:String,
        val wooden_frames_glassses:String,
        val water_glass:String,
        val cookies:String,
        val refrigrator:String,
        val floor_wall_tiles:String,
        val bar_tables_chains:String,
        val steel_dustbins:String,
        val pril_liquid:String,
        val h_k_bor_signature:String,
        val h_K_sup_signature:String,
        val refregerator_cleening:String,
        val oven_cleaning:String,
        val sink_cleaning:String,
        val snacks:String,
        @PrimaryKey(autoGenerate = true) val id: Int? = null
    )
  @Entity(tableName = Constants.pantry_tasks_table)
   data class PantryTasks(
            val task_name:String="",
            val created_date:String="",
            val isAssigned:Boolean=false,
            val isCompleted:Boolean= false,
            @PrimaryKey(autoGenerate = true) val id: Int? = null
        )
@Entity(tableName = "restroom_table")
data class RestRoomEntityData(
    val Door:Boolean,
    val Lights:String,
    val Dust_Bin:String,
    val W_C:String,
    val Toilet_Roll:String,
    val Urinal:String,
    val Mirror:String,
    val Wash_Basin:String,
    val Taps:String,
    val Liquid_soap:String,
    val Floor_Side_Tiles:String,
    val Floor_Trap:String,
    val Exhaust:String,
    val c_Fold:String,
    val Handicap_Toilet:String,
    val Hand_Drier:String,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)
@Entity(tableName = Constants.restroom_tasks_table)
data class RestRoomTasks(
    val task_name:String="",
    val created_date:String="",
    val created_time:String="",
    var isAssignedTo:String="",
    val isAssigned:Boolean=false,
    val isCompleted:Boolean= false,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)