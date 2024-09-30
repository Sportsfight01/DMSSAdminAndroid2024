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

    @Query("SELECT * from ASSIGN_TASK_TO_PERSON ")
    fun getAssignTaskToPersons(): List<AssignTaskToPersonEntityData>
    @Delete
    suspend fun deletedAssignTaskToPerson(assignTaskToPersonEntityData: AssignTaskToPersonEntityData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPantryData(pantryEntityData: PantryEntityData)
    @Delete
    suspend fun deletedPantryData(pantryEntityData: PantryEntityData)
//    @Query("UPDATE pantry_table set isCompleted = :isCompleted where create_date = :createDate")
//    suspend fun updatePantryData( createDate: String?, isCompleted: Boolean?)
    @Query("SELECT * from pantry_table ")
    fun getAllPantryData(): LiveData<List<PantryEntityData>>



    @RawQuery
    suspend fun rawQuery(theQuery: SimpleSQLiteQuery): List<String>
    suspend fun getAColumnFromATable(): List<String> {
        return rawQuery(SimpleSQLiteQuery("SELECT DISTINCT name FROM pragma_table_info('pantry_table') c order by name"))
    }


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertPantryData(pantryEntityData: PantryTasks)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPantryData(pantryEntityData: PantryTasks)
    @Delete
    suspend fun deletedPantryData(pantryEntityData: PantryTasks)

    @Query("UPDATE pantry_tasks_table set isCompleted = :isCompleted,isAssigned= :isAssigned where task_name=:taskName and created_date = :createDate and created_time = :time")
    suspend fun updatePantryTasksData( taskName: String?,createDate: String?, time: String?,isCompleted: Boolean?,isAssigned: Boolean)

    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:arg0 order by task_name")
    fun getAllPPantryTasks(arg0: String): LiveData<List<PantryTasks>>

    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:date and created_time=:time and isAssigned=:isAssigned and isCompleted=:isCompleted order by task_name")
    fun getAllPPantryTasksAssigned(date: String,time: String,isAssigned: Boolean,isCompleted: Boolean): List<PantryTasks>


    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:arg0 and created_time = :time  and isAssigned=:isAssigned and isCompleted=:isCompleted order by task_name")
    fun getAllPantryTasksCompleted(arg0: String,time: String,isAssigned: Boolean,isCompleted: Boolean): List<PantryTasks>

    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:arg0 and created_time = :time  and isAssigned=:isAssigned order by task_name")
    fun getAllPantryTasksNotAssigned(arg0: String,time: String,isAssigned: Boolean): List<PantryTasks>

    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:arg0 and created_time = :time and isAssigned=:isAssigned order by task_name")
    fun getAllPPantryTasks1(arg0: String,time: String,isAssigned: Boolean): List<PantryTasks>

    @Query("SELECT * FROM pantry_tasks_table WHERE created_date=:arg0 order by task_name")
    fun getAllPPantryTasksByDate(arg0: String): List<PantryTasks>

    @Query("SELECT * FROM pantry_tasks_table order by task_name")
    fun getAllPPantryTasks1(): List<PantryTasks>

    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:arg0 and created_time = :time  and isAssigned=:isAssigned order by task_name")
    fun getAllMaleRestRoomTasksNotAssigned(arg0: String,time: String,isAssigned: Boolean): List<RestRoomTasks>

    @Query("SELECT * FROM female_restroom_tasks_table WHERE created_date=:arg0 and created_time = :time  and isAssigned=:isAssigned order by task_name")
    fun getAllFeMaleRestRoomTasksNotAssigned(arg0: String,time: String,isAssigned: Boolean): List<FemaleRestRoomTasks>
    @RawQuery
    suspend fun rawQueryRestroomTable(theQuery: SimpleSQLiteQuery): List<String>
    suspend fun getAColumnFromRestRoomTable(): List<String> {
        return rawQueryRestroomTable(SimpleSQLiteQuery("SELECT name FROM pragma_table_info('restroom_table') c order by name"))
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

    @Query("UPDATE restroom_tasks_table set isCompleted = :isCompleted,isAssigned= :isAssigned where task_name=:taskName and created_date = :createDate and created_time = :time")
    suspend fun updateRestRoomTasksData(taskName: String?, createDate: String?,time: String,isAssigned:Boolean, isCompleted: Boolean?)

    @Query("UPDATE restroom_tasks_table set isCompleted = :isCompleted and isAssigned= :isAssined where created_date = :createDate")
    suspend fun updateRestRoomPendingTasksData( createDate: String?,isAssined: Boolean?, isCompleted: Boolean?)

    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:arg0 order by task_name")
    fun getAllRestRoomTasks(arg0: String): LiveData<List<RestRoomTasks>>

    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:date and  created_time=:time and isAssigned=:isAssigned and isCompleted=:isCompleted order by task_name")
    fun getAllRestRoomTasksAssigned(date: String,time: String,isAssigned: Boolean,isCompleted: Boolean): List<RestRoomTasks>


    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:date and created_time= :time and isAssigned=:isAssigned and isCompleted=:isCompleted order by task_name")
    fun getAllRestRoomTasksCompleted(date: String,time: String,isAssigned: Boolean,isCompleted: Boolean): List<RestRoomTasks>

    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:date and created_time = :time and isAssigned=:isAssigned order by task_name")
    fun getAllRestRoomTasks(date: String,time: String,isAssigned: Boolean): List<RestRoomTasks>

    @Query("SELECT * FROM restroom_tasks_table WHERE created_date=:arg0 order by task_name")
    fun getAllRestRoomTasksByDateWise(arg0: String): List<RestRoomTasks>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFemaleRestRoomTasksData(restRoomTasks: FemaleRestRoomTasks)

    @Query("SELECT * FROM female_restroom_tasks_table WHERE created_date=:date and  created_time=:time and isAssigned=:isAssigned and isCompleted=:isCompleted order by task_name")
    fun getFemaleRestRoomTasksByDateTime(date: String,time: String,isAssigned: Boolean,isCompleted: Boolean): List<FemaleRestRoomTasks>

    @Query("SELECT * FROM female_restroom_tasks_table WHERE created_date=:date and  created_time=:time and isAssigned=:isAssigned order by task_name")
    fun getFemaleRestRoomAssignedTask(date: String,time: String,isAssigned: Boolean): List<FemaleRestRoomTasks>

    @Delete
    suspend fun deleteFemaleRestRoomTasksData(restRoomTasks: FemaleRestRoomTasks)

    @Query("UPDATE female_restroom_tasks_table set isCompleted = :isCompleted,isAssigned= :isAssigned where task_name=:taskName and created_date = :createDate and created_time = :time")
    suspend fun updateFemaleRestRoomTasksData(taskName: String?, createDate: String?,time: String, isAssigned: Boolean,isCompleted: Boolean?)

    @Query("SELECT * FROM female_restroom_tasks_table WHERE created_date=:date order by task_name")
    fun getFemaleRestRoomTasksByDate(date: String): List<FemaleRestRoomTasks>
}