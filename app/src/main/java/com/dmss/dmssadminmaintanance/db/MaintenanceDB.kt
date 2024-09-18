package com.dmss.dmssadminmaintanance.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(AssignTaskToPersonEntityData::class,PantryEntityData::class,PantryTasks::class,
RestRoomEntityData::class,RestRoomTasks::class,FemaleRestRoomTasks::class), version = 1, exportSchema = false)
abstract class MaintenanceDB:RoomDatabase() {

    abstract fun getMaintenanceDao():MaintainanceDao

        companion object{
            @Volatile
            private var INSTANCE:MaintenanceDB?=null
           var DATABASE_NAME="dmss_maintenance_db"

            fun getDatabase(context: Context):MaintenanceDB{

                return INSTANCE?: synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MaintenanceDB::class.java,
                        DATABASE_NAME
                    ).build()
                    INSTANCE = instance
                    instance
                }
            }

        }


}