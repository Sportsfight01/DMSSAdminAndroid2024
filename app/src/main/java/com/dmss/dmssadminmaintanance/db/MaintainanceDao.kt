package com.dmss.dmssadminmaintanance.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery


@Dao
interface MaintainanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignTaskToPerson(assignTaskToPersonEntityData: AssignTaskToPersonEntityData)

    @Query("SELECT * from ASSIGN_TASK_TO_PERSON")
    fun getAssignTaskToPersons(): List<AssignTaskToPersonEntityData>
    @Delete
    suspend fun deletedAssignTaskToPerson(assignTaskToPersonEntityData: AssignTaskToPersonEntityData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPantryData(pantryEntityData: PantryEntityData)
    @Delete
    suspend fun deletedPantryData(pantryEntityData: PantryEntityData)
//    @Query("UPDATE pantry_table set isCompleted = :isCompleted where create_date = :createDate")
//    suspend fun updatePantryData( createDate: String?, isCompleted: Boolean?)
    @Query("SELECT * from pantry_table")
    fun getAllPantryData(): LiveData<List<PantryEntityData>>



    @RawQuery
    suspend fun rawQuery(theQuery: SimpleSQLiteQuery): List<String>
    suspend fun getAColumnFromATable(): List<String> {
        return rawQuery(SimpleSQLiteQuery("SELECT DISTINCT name FROM pragma_table_info('pantry_table') c"))
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPantryData(pantryEntityData: PantryTasks)

    @Delete
    suspend fun deletedPantryData(pantryEntityData: PantryTasks)

    @Query("UPDATE pantry_tasks_table set isCompleted = :isCompleted where task_name=:taskName and created_date = :createDate")
    suspend fun updatePantryTasksData( taskName: String?,createDate: String?, isCompleted: Boolean?)

    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:arg0")
    fun getAllPPantryTasks(arg0: String): LiveData<List<PantryTasks>>

    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:arg0 and isAssigned=:isAssigned and isCompleted=:isCompleted")
    fun getAllPPantryTasksAssigned(arg0: String,isAssigned: Boolean,isCompleted: Boolean): List<PantryTasks>


    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:arg0 and isAssigned=:isAssigned and isCompleted=:isCompleted")
    fun getAllPantryTasksCompleted(arg0: String,isAssigned: Boolean,isCompleted: Boolean): List<PantryTasks>

    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:arg0 and isAssigned=:isAssigned")
    fun getAllPPantryTasks1(arg0: String,isAssigned: Boolean): List<PantryTasks>

    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:arg0")
    fun getAllPPantryTasksByDate(arg0: String): List<PantryTasks>

    @Query("SELECT * FROM pantry_tasks_table")
    fun getAllPPantryTasks1(): List<PantryTasks>


    @RawQuery
    suspend fun rawQueryRestroomTable(theQuery: SimpleSQLiteQuery): List<String>
    suspend fun getAColumnFromRestRoomTable(): List<String> {
        return rawQueryRestroomTable(SimpleSQLiteQuery("SELECT name FROM pragma_table_info('restroom_table') c"))
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestRoomColumData(restRoomEntityData: RestRoomEntityData)

    @Delete
    suspend fun deletedRoomColumData(restRoomEntityData: RestRoomEntityData)

    @Query("UPDATE restroom_tasks_table set isCompleted = :isCompleted where created_date = :createDate")
    suspend fun updateRoomColumData( createDate: String?, isCompleted: Boolean?)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestRoomTasksData(restRoomTasks: RestRoomTasks)

    @Delete
    suspend fun deletedRestRoomTasksData(restRoomTasks: RestRoomTasks)

    @Query("UPDATE restroom_tasks_table set   isCompleted = :isCompleted where created_date = :createDate and task_name= :taskName")
    suspend fun updateRestRoomTasksData(taskName: String?, createDate: String?, isCompleted: Boolean?)

    @Query("UPDATE restroom_tasks_table set isCompleted = :isCompleted and isAssigned= :isAssined where created_date = :createDate")
    suspend fun updateRestRoomPendingTasksData( createDate: String?,isAssined: Boolean?, isCompleted: Boolean?)

    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:arg0")
    fun getAllRestRoomTasks(arg0: String): LiveData<List<RestRoomTasks>>

    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:date and  created_time=:time and isAssigned=:isAssigned and isCompleted=:isCompleted")
    fun getAllRestRoomTasksAssigned(date: String,time: String,isAssigned: Boolean,isCompleted: Boolean): List<RestRoomTasks>


    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:date and created_time= :time and isAssigned=:isAssigned and isCompleted=:isCompleted")
    fun getAllRestRoomTasksCompleted(date: String,time: String,isAssigned: Boolean,isCompleted: Boolean): List<RestRoomTasks>

    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:date and created_time = :time and isAssigned=:isAssigned")
    fun getAllRestRoomTasks(date: String,time: String,isAssigned: Boolean): List<RestRoomTasks>

    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:arg0")
    fun getAllRestRoomTasksByDateWise(arg0: String): List<RestRoomTasks>


}