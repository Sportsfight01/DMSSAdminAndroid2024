package com.dmss.admin.db.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmss.dmssadminmaintanance.db.AssignTaskToPersonEntityData
import com.dmss.dmssadminmaintanance.db.FemaleRestRoomTasks
import com.dmss.dmssadminmaintanance.db.MaintainanceDao
import com.dmss.dmssadminmaintanance.db.PantryEntityData
import com.dmss.dmssadminmaintanance.db.PantryTasks
import com.dmss.dmssadminmaintanance.db.RestRoomEntityData
import com.dmss.dmssadminmaintanance.db.RestRoomTasks


class MaintananceRepository(private val maintainanceDao: MaintainanceDao) {
//    val allPantriesData:LiveData<List<PantryEntityData>> = maintainanceDao.getAllPantryData()

    suspend fun getAssignTaskToPersonsData(): List<AssignTaskToPersonEntityData> {
        return maintainanceDao.getAssignTaskToPersons()
    }
    suspend fun insertTaskAssigningPersons(assignTaskToPersonEntityData: AssignTaskToPersonEntityData){
        maintainanceDao.insertAssignTaskToPerson(assignTaskToPersonEntityData)
    }
    suspend fun deleteTaskAssigningPersons(assignTaskToPersonEntityData: AssignTaskToPersonEntityData){
        maintainanceDao.deletedAssignTaskToPerson(assignTaskToPersonEntityData)
    }

    suspend fun getAColumnFromATable(): List<String> {
       return maintainanceDao.getAColumnFromATable()
    }
    suspend fun insertPantry(pantryEntityData: PantryEntityData){
        maintainanceDao.insertPantryData(pantryEntityData)
    }
    suspend fun deletePantry(pantryEntityData: PantryEntityData){
        maintainanceDao.deletedPantryData(pantryEntityData)
    }
    suspend fun updatePantry(pantryEntityData: PantryEntityData){
//        maintainanceDao.updatePantryData(pantryEntityData.create_date,pantryEntityData.isCompleted)
    }

    suspend fun insertPantryTasks(pantryTasks: PantryTasks){
        maintainanceDao.insertPantryData(pantryTasks)
    }
    suspend fun deletePantryTasks(pantryTasks: PantryTasks){
        maintainanceDao.deletedPantryData(pantryTasks)
    }
    suspend fun updatePantryTasks(pantryEntityData: PantryTasks){
        maintainanceDao.updatePantryTasksData(pantryEntityData.task_name,pantryEntityData.created_date,pantryEntityData.created_time,pantryEntityData.isCompleted,pantryEntityData.isAssigned)
    }
     fun getAllPantryTasksAssigned(date: String,time:String,isAssigned:Boolean,isCompleted:Boolean): List<PantryTasks> {
         var res1=maintainanceDao.getAllPPantryTasksAssigned(date,time,isAssigned,isCompleted)
         println("getAllPPantryTasks getAllPPantryTasks1 ::  "+res1)

      /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
         println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }
    fun getAllPantryTasksByDate(date: String): List<PantryTasks> {
        var res1=maintainanceDao.getAllPPantryTasksByDate(date)
        println("getAllPPantryTasks getAllPPantryTasks1 ::  "+res1)

        /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
           println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }
    fun getAllPantryTasks(date: String,time: String,isAssigned:Boolean): List<PantryTasks> {
        var res1=maintainanceDao.getAllPPantryTasks1(date,time,isAssigned)
        println("getAllPPantryTasks getAllPPantryTasks1 ::  "+res1)

        /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
           println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }
    fun getAllPantryNotAssignedTasks(date: String,time: String,isAssigned:Boolean): List<PantryTasks> {
        var res1=maintainanceDao.getAllPantryTasksNotAssigned(date,time,isAssigned)
        println("getAllPPantryTasks getAllPPantryTasks1 ::  "+" isAssigned:: "+isAssigned+"  "+res1)

        /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
           println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }
    fun getAllMaleRestRoomNotAssignedTasks(date: String,time: String,isAssigned:Boolean): List<RestRoomTasks> {
        var res1=maintainanceDao.getAllMaleRestRoomTasksNotAssigned(date,time,isAssigned)
        println("getAllPPantryTasks getAllPPantryTasks1 ::  "+" isAssigned:: "+isAssigned+"  "+res1)

        /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
           println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }
    fun getAllFeMaleRestRoomNotAssignedTasks(date: String,time: String,isAssigned:Boolean): List<FemaleRestRoomTasks> {
        var res1=maintainanceDao.getAllFeMaleRestRoomTasksNotAssigned(date,time,isAssigned)
        println("getAllPPantryTasks getAllPPantryTasks1 ::  "+" isAssigned:: "+isAssigned+"  "+res1)

        /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
           println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }
    fun getAllPantryTasksCompleted(date: String,time: String,isAssigned:Boolean,isCompleted:Boolean): List<PantryTasks> {
        var res1=maintainanceDao.getAllPantryTasksCompleted(date,time,isAssigned,isCompleted)
        println("getAllPPantryTasks getAllPPantryTasks1 ::  "+res1)

        /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
           println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }


    suspend fun getAColumnFromRestRoomTable(): List<String> {
        return maintainanceDao.getAColumnFromRestRoomTable()
    }
    suspend fun insertRestRoomColumData(restRoomEntityData: RestRoomEntityData){
        maintainanceDao.insertRestRoomColumData(restRoomEntityData)
    }
    suspend fun deletedRoomColumData(restRoomEntityData: RestRoomEntityData){
        maintainanceDao.deletedRoomColumData(restRoomEntityData)
    }
    suspend fun updateRoomColumData(pantryEntityData: PantryEntityData){
//        maintainanceDao.updatePantryData(pantryEntityData.create_date,pantryEntityData.isCompleted)
    }


    suspend fun insertRestRoomTasks(restRoomTasks: RestRoomTasks){
        maintainanceDao.insertRestRoomTasksData(restRoomTasks)
    }
    suspend fun deleteRestRoomTasks(restRoomTasks: RestRoomTasks){
        maintainanceDao.deletedRestRoomTasksData(restRoomTasks)
    }
    suspend fun updateRestRoomTasksData(restRoomTasks: RestRoomTasks){
        maintainanceDao.updateRestRoomTasksData(restRoomTasks.task_name,restRoomTasks.created_date,restRoomTasks.created_time,restRoomTasks.isAssigned,restRoomTasks.isCompleted)
    }
    suspend fun updateRestRoomPendingTasksData(date: String,isAssigned:Boolean,isCompleted:Boolean){
        maintainanceDao.updateRestRoomPendingTasksData(date,isAssigned,isCompleted)
    }
    fun getAllRestRoomTasksAssigned(date: String,time: String,isAssigned:Boolean,isCompleted:Boolean): List<RestRoomTasks> {
        var res1=maintainanceDao.getAllRestRoomTasksAssigned(date,time,isAssigned,isCompleted)
        println("getAllPPantryTasks getAllPPantryTasks1 ::  "+res1)

        /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
           println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }
    fun getAllRestRoomTasks(date: String,time: String,isAssigned:Boolean): List<RestRoomTasks> {
        var res1=maintainanceDao.getAllRestRoomTasks(date,time,isAssigned)
        println("getAllPPantryTasks getAllPPantryTasks1 ::  "+res1)

        /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
           println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }
    fun getAllRestRoomTasksCompleted(date: String,time: String,isAssigned:Boolean,isCompleted:Boolean): List<RestRoomTasks> {
        var res1=maintainanceDao.getAllRestRoomTasksCompleted(date,time,isAssigned,isCompleted)
        println("getAllPPantryTasks getAllPPantryTasks1 ::  "+res1)

        /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
           println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }
    fun getAllRestRoomTasksByDateWise(date: String): List<RestRoomTasks> {
        var res1=maintainanceDao.getAllRestRoomTasksByDateWise(date)
        println("getAllPPantryTasks getAllPPantryTasks1 ::  "+res1)

        /*   var res=maintainanceDao.getAllPPantryTasks("12-08-2024")
           println("getAllPPantryTasks::  "+res.value)*/
        return res1
    }
    suspend fun insertFemaleREstrom(femaleRestRoomTasks: FemaleRestRoomTasks){
        maintainanceDao.insertFemaleRestRoomTasksData(femaleRestRoomTasks)
    }
    suspend fun deleteFemaleRestRoomTasks(restRoomTasks: FemaleRestRoomTasks){
        maintainanceDao.deleteFemaleRestRoomTasksData(restRoomTasks)
    }
    suspend fun deletedAssignTaskToPerson(assignTaskToPersonEntityData: AssignTaskToPersonEntityData){
        maintainanceDao.deletedAssignTaskToPerson(assignTaskToPersonEntityData)
    }
    fun getFemaleRestRoomTask(date: String,time: String,isAssigned: Boolean,isCompleted: Boolean):List<FemaleRestRoomTasks>{

        var res = maintainanceDao.getFemaleRestRoomTasksByDateTime(date,time,isAssigned,isCompleted)

        return res
    }
    fun getFemaleRestRoomTaskByDate(date: String):List<FemaleRestRoomTasks>{

        var res = maintainanceDao.getFemaleRestRoomTasksByDate(date)

        return res
    }
    fun getFemaleRestRoomAssignedTask(date: String,time: String,isAssigned: Boolean):List<FemaleRestRoomTasks>{

        var res = maintainanceDao.getFemaleRestRoomAssignedTask(date,time,isAssigned)

        return res
    }
    suspend fun updateFemaleRestRoomTasksData(restRoomTasks: FemaleRestRoomTasks){
        maintainanceDao.updateFemaleRestRoomTasksData(restRoomTasks.task_name,restRoomTasks.created_date,restRoomTasks.created_time,restRoomTasks.isAssigned,restRoomTasks.isCompleted)
    }
}