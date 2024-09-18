package com.dmss.admin.db.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dmss.dmssadminmaintanance.db.AssignTaskToPersonEntityData
import com.dmss.dmssadminmaintanance.db.FemaleRestRoomTasks

import com.dmss.dmssadminmaintanance.db.MaintenanceDB
import com.dmss.dmssadminmaintanance.db.PantryEntityData
import com.dmss.dmssadminmaintanance.db.PantryTasks
import com.dmss.dmssadminmaintanance.db.RestRoomTasks
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MaintainanceViewModel(application: Application) : AndroidViewModel(application!!){
// class MaintainanceViewModel(application: Application) :ViewModel(){
   val allPantryTasksDataBydate=MutableLiveData<List<PantryTasks>>()
    val allPantryPendingTasksByDate=MutableLiveData<List<PantryTasks>>()
    val allPantryCompletedTasksByDate=MutableLiveData<List<PantryTasks>>()
    val allPantryTasksByDate=MutableLiveData<List<PantryTasks>>()

    val allRestRoomTasksDataBydate=MutableLiveData<List<RestRoomTasks>>()

    val allRestRoomPendingTasksDataBydate=MutableLiveData<List<RestRoomTasks>>()
    val allRestRoomCompletedTasksDataBydate=MutableLiveData<List<RestRoomTasks>>()
    val allRestRoomTasksByDateWise=MutableLiveData<List<RestRoomTasks>>()
    val femaleRestRoomTasksByDateTimeWise=MutableLiveData<List<FemaleRestRoomTasks>>()
    val femaleRestRoomAssignedTasksByDateTimeWise=MutableLiveData<List<FemaleRestRoomTasks>>()
    val femaleRestRoomCompletedTasksByDateTimeWise=MutableLiveData<List<FemaleRestRoomTasks>>()



    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    var job: Job?=null
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    private val maintananceRepository: MaintananceRepository
//    var allPantryDataBydate = LiveData<List<PantryEntityData>> =null
    var allPantryDataBydate : LiveData<List<PantryEntityData>>? = null
     val pantryAllCoulumns: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    val restRoomsAllCoulumns: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    val assignTaskPersons: MutableLiveData<List<AssignTaskToPersonEntityData>>  = MutableLiveData<List<AssignTaskToPersonEntityData>>()
    val resourcesLiveData by lazy { MutableLiveData<MutableList<List<String>>>() }
//    var allPantryTasksDataBydate : LiveData<List<PantryTasks>>? = null

    init {
        val dao = MaintenanceDB.getDatabase(application).getMaintenanceDao()
        maintananceRepository = MaintananceRepository(dao)
//        allPantryDataBydate =maintananceRepository.allPantriesData
//        allPantryTasksDataBydate =maintananceRepository.allPantryTasksDataBydate

//        resourcesLiveData.value?.add(allPantryColumnNames!!)
//        AllPantryColumnNames()
//        AllRestRoomColumnNames()
    }
    fun insertTaskAssigningPersons(assignTaskToPersonEntityData: AssignTaskToPersonEntityData) = viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.insertTaskAssigningPersons(assignTaskToPersonEntityData)
    }
    fun getAssignTaskPersons():LiveData<List<AssignTaskToPersonEntityData>>{

        return assignTaskPersons
    }
    fun getAllPantryTasksByDate(): LiveData<List<PantryTasks>> {
        return allPantryTasksDataBydate
    }
    fun getAllPantryPendingTasksByDate(): LiveData<List<PantryTasks>> {
        return allPantryPendingTasksByDate
    }
    fun getAllPantryCompletedTasksByDate(): LiveData<List<PantryTasks>> {
        return allPantryCompletedTasksByDate
    }
    fun getAllRestRoomTasksByDate(): LiveData<List<RestRoomTasks>> {
        return allRestRoomTasksDataBydate
    }
    fun getAllRestRoomPendingTasksDataBydate(): LiveData<List<RestRoomTasks>> {
        return allRestRoomPendingTasksDataBydate
    }
    fun getAllRestRoomCompletedTasksDataBydate(): LiveData<List<RestRoomTasks>> {
        return allRestRoomCompletedTasksDataBydate
    }
    fun getAllRestRoomTasksByDateWise(): LiveData<List<RestRoomTasks>> {
        return allRestRoomTasksByDateWise
    }
    fun getAllPantryTasksByDateWise(): LiveData<List<PantryTasks>> {
        return allPantryTasksByDate
    }

   fun getAllPantryColumnns() :LiveData<List<String>>{
      return pantryAllCoulumns
  }
     fun getAllRestRoomColumnData()  :LiveData<List<String>>{
       return restRoomsAllCoulumns
    }
    fun insertPantry(pantryEntityData: PantryEntityData) = viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.insertPantry(pantryEntityData)
    }
    fun deletePantry(pantryEntityData: PantryEntityData) = viewModelScope.launch (Dispatchers.IO){
        maintananceRepository.deletePantry(pantryEntityData)
    }
    fun updatePantry(pantryEntityData: PantryEntityData) =viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.updatePantry(pantryEntityData)
    }
    fun getFemaleRestroomTasksByDateTime(): LiveData<List<FemaleRestRoomTasks>>{

        return femaleRestRoomTasksByDateTimeWise
    }
    fun getFemaleRestroomAssignedTasksByDateTime(): LiveData<List<FemaleRestRoomTasks>>{

        return femaleRestRoomAssignedTasksByDateTimeWise
    }
    fun getFemaleRestroomCompletedTasksByDateTime(): LiveData<List<FemaleRestRoomTasks>>{

        return femaleRestRoomCompletedTasksByDateTimeWise
    }

    /*fun insertPantryTasks(pantryTasks: PantryTasks) = viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.insertPantryTasks(pantryTasks)
    }*/
    fun insertPantryTasks(pantryTasks: PantryTasks) = viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.insertPantryTasks(pantryTasks)
    }
    fun deletePantryTasks(pantryTasks: PantryTasks) = viewModelScope.launch (Dispatchers.IO){
        maintananceRepository.deletePantryTasks(pantryTasks)
    }
    fun updatePantryTasks(pantryTasks: PantryTasks) =viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.updatePantryTasks(pantryTasks)
    }
    fun deletedAssignTaskToPerson(assignTaskToPersonEntityData: AssignTaskToPersonEntityData) = viewModelScope.launch (Dispatchers.IO){
        maintananceRepository.deletedAssignTaskToPerson(assignTaskToPersonEntityData)
    }

    fun getAssignedPersonRequest():LiveData<List<AssignTaskToPersonEntityData>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {

            val res = maintananceRepository.getAssignTaskToPersonsData()

            assignTaskPersons.postValue(res)
        }
        return assignTaskPersons
    }
    fun getAllPantryTasksbydate(date:String): LiveData<List<PantryTasks>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val res=maintananceRepository.getAllPantryTasksByDate(date)
            var resLivdata=MutableLiveData<List<PantryTasks>>()
            resLivdata.postValue(res)
            println("getAllPantryTasksbydate1:: "+res)
            allPantryTasksByDate.postValue(res)
        }
        return allPantryTasksByDate
    }
    fun getAllPantryTasksbydate1(date:String,time: String,isAssigned:Boolean): LiveData<List<PantryTasks>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val res=maintananceRepository.getAllPantryTasks(date,time,isAssigned)
            var resLivdata=MutableLiveData<List<PantryTasks>>()
            resLivdata.postValue(res)
            println("getAllPantryTasksbydate1:: "+res)
            allPantryTasksDataBydate.postValue(res)
        }
        return allPantryTasksDataBydate
    }
    fun getAllPantryTasksCompeted(date:String,time:String,isAssigned:Boolean,isCompleted:Boolean): LiveData<List<PantryTasks>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val res=maintananceRepository.getAllPantryTasksCompleted(date,time
                    ,isAssigned,isCompleted)
            var resLivdata=MutableLiveData<List<PantryTasks>>()
            resLivdata.postValue(res)
            println("getAllPantryTasksbydate1:: "+res)
            allPantryCompletedTasksByDate.postValue(res)
        }
        return allPantryCompletedTasksByDate
    }
    fun getAllPantryTasksAssigned(date:String,time:String,isAssigned:Boolean,isCompleted:Boolean): LiveData<List<PantryTasks>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val res=maintananceRepository.getAllPantryTasksAssigned(date,time,isAssigned,isCompleted)
            var resLivdata=MutableLiveData<List<PantryTasks>>()
            resLivdata.postValue(res)
            println("getAllPantryTasksbydate1:: "+res)
            allPantryPendingTasksByDate.postValue(res)
        }
        return allPantryPendingTasksByDate
    }
    fun getAllPantryColumnsRequest(): LiveData<List<String>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            pantryAllCoulumns.postValue(maintananceRepository.getAColumnFromATable())

          /*  val res=maintananceRepository.getAllPantryTasksAssigned(date,isAssigned,isCompleted)
            var resLivdata=MutableLiveData<List<PantryTasks>>()
            resLivdata.postValue(res)
            println("getAllPantryTasksbydate1:: "+res)
            allPantryPendingTasksByDate.postValue(res)*/
        }
        return pantryAllCoulumns
    }

    fun getAllRestroomColumns(): LiveData<List<String>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            restRoomsAllCoulumns.postValue(maintananceRepository.getAColumnFromRestRoomTable())

            /*  val res=maintananceRepository.getAllPantryTasksAssigned(date,isAssigned,isCompleted)
              var resLivdata=MutableLiveData<List<PantryTasks>>()
              resLivdata.postValue(res)
              println("getAllPantryTasksbydate1:: "+res)
              allPantryPendingTasksByDate.postValue(res)*/
        }
        return restRoomsAllCoulumns
    }

    fun getAllRestRoomTasks(date:String,time:String,isAssigned:Boolean): LiveData<List<RestRoomTasks>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val res=maintananceRepository.getAllRestRoomTasks(date,time,isAssigned)
            var resLivdata=MutableLiveData<List<RestRoomTasks>>()
            resLivdata.postValue(res)
            println("getAllPantryTasksbydate1:: "+res)
            allRestRoomTasksDataBydate.postValue(res)
        }
        return allRestRoomTasksDataBydate
    }
    fun getAllRestRoomTasksCompleted(date:String,time: String,isAssigned:Boolean,isCompleted:Boolean): LiveData<List<RestRoomTasks>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val res=maintananceRepository.getAllRestRoomTasksCompleted(date,time,isAssigned,isCompleted)
            var resLivdata=MutableLiveData<List<RestRoomTasks>>()
            resLivdata.postValue(res)
            println("getAllPantryTasksbydate1:: "+res)
            allRestRoomCompletedTasksDataBydate.postValue(res)
        }
        return allRestRoomCompletedTasksDataBydate
    }
    fun getAllRestRoomTasksAssigned(date:String,time:String,isAssigned:Boolean,isCompleted:Boolean): LiveData<List<RestRoomTasks>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val res=maintananceRepository.getAllRestRoomTasksAssigned(date,time,isAssigned,isCompleted)
            var resLivdata=MutableLiveData<List<RestRoomTasks>>()
            resLivdata.postValue(res)
            println("getAllPantryTasksbydate1:: "+res)
            allRestRoomPendingTasksDataBydate.postValue(res)
        }
        return allRestRoomPendingTasksDataBydate
    }
    fun getAllRestRoomTasksDateWise(date:String): LiveData<List<RestRoomTasks>>{
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val res=maintananceRepository.getAllRestRoomTasksByDateWise(date)
            var resLivdata=MutableLiveData<List<RestRoomTasks>>()
            resLivdata.postValue(res)
            println("getAllPantryTasksbydate1:: "+res)
            allRestRoomTasksByDateWise.postValue(res)
        }
        return allRestRoomTasksByDateWise
    }
    fun sendRequestToAllFemaleRestRoomData(date: String):LiveData<List<FemaleRestRoomTasks>>{

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {

            val res = maintananceRepository.getFemaleRestRoomTaskByDate(date)

            femaleRestRoomTasksByDateTimeWise.postValue(res)
        }
        return femaleRestRoomAssignedTasksByDateTimeWise
    }
    fun sendRequestToFemaleRestRoomOnlyAssignedTasks(date: String,time: String,isAssigned: Boolean,isCompleted: Boolean):LiveData<List<FemaleRestRoomTasks>>{

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {

            val res = maintananceRepository.getFemaleRestRoomTask(date, time, isAssigned, isCompleted)

            femaleRestRoomAssignedTasksByDateTimeWise.postValue(res)
        }
        return femaleRestRoomAssignedTasksByDateTimeWise
    }
    fun sendRequestToFemaleRestRoomAssignedTasks(date: String,time: String,isAssigned: Boolean):LiveData<List<FemaleRestRoomTasks>>{

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {

            val res = maintananceRepository.getFemaleRestRoomAssignedTask(date, time, isAssigned)
            println("sendRequestToFemaleRestRoomAssignedTasks::"+date+"  time:: "+" isAssigned:: "+isAssigned+"  RES:: "+res)

            femaleRestRoomTasksByDateTimeWise.postValue(res)
        }
        return femaleRestRoomTasksByDateTimeWise
    }
    fun sendRequestToFemaleRestRoomCompletedTasks(date: String,time: String,isAssigned: Boolean,isCompleted: Boolean):LiveData<List<FemaleRestRoomTasks>>{

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {

            val res = maintananceRepository.getFemaleRestRoomTask(date, time, isAssigned, isCompleted)
            println("sendRequestToFemaleRestRoomAssignedTasks::"+date+"  time:: "+" isAssigned:: "+isAssigned+"  RES:: "+res)

            femaleRestRoomCompletedTasksByDateTimeWise.postValue(res)
        }
        return femaleRestRoomCompletedTasksByDateTimeWise
    }
    fun insertFemaleRestRoomTasks(femaleRestRoomTasks: FemaleRestRoomTasks) = viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.insertFemaleREstrom(femaleRestRoomTasks)
    }
    fun insertRestRoomTasks(restRoomTasks: RestRoomTasks) = viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.insertRestRoomTasks(restRoomTasks)
    }
    fun deleteRestRoomTasks(restRoomTasks: RestRoomTasks) = viewModelScope.launch (Dispatchers.IO){
        maintananceRepository.deleteRestRoomTasks(restRoomTasks)
    }
    fun updateRestRoomTasks(restRoomTasks: RestRoomTasks) =viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.updateRestRoomTasksData(restRoomTasks)
    }
    fun updateFemaleRestRoomTasks(restRoomTasks: FemaleRestRoomTasks) =viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.updateFemaleRestRoomTasksData(restRoomTasks)
    }
    fun updateRestRoomPendingTasks(date: String,isAssigned:Boolean,isCompleted:Boolean) =viewModelScope.launch(Dispatchers.IO){
        maintananceRepository.updateRestRoomPendingTasksData(date,isAssigned,isCompleted)
    }
    private fun onError(message: String) {
        errorMessage.postValue( message)
        loading.postValue( false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}