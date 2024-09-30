package com.dmss.dmssadminmaintanance.sidemenu

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.databinding.FragmentDownloadExcelsBinding
import com.dmss.dmssadminmaintanance.databinding.LayoutDailogListViewBinding
import com.dmss.dmssadminmaintanance.db.FemaleRestRoomTasks
import com.dmss.dmssadminmaintanance.db.PantryTasks
import com.dmss.dmssadminmaintanance.db.RestRoomTasks
import com.dmss.dmssadminmaintanance.model.SharedPreferencesManager
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.CommanAdapter
import com.dmss.dmssadminmaintanance.xcelsheet.Constants
import com.dmss.dmssadminmaintanance.xcelsheet.ExcelUtils
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 * Use the [DownloadExcelsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DownloadExcelsFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentDownloadExcelsBinding
    var tasksList = ArrayList<String>()
    var listOfRestRoomColumns = ArrayList<String>()
    var listOfTimingsColumns = ArrayList<String>()
    var listOfPantryColumns = ArrayList<String>()
    var  maleRestRoomTasks=ArrayList<RestRoomTasks>()
     private lateinit var viewModel: MaintainanceViewModel
    var selectedDate=""
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDownloadExcelsBinding.inflate(inflater, container, false)

        initView()
        requestViewModel()
        Observers()
        return binding.root
    }
    private fun initView(){

//        tasksList.add(getString(R.string.cabins))
//        tasksList.add(getString(R.string.games_room))
//        tasksList.add(getString(R.string.work_station))
//        tasksList.add(getString(R.string.pantry))
//        tasksList.add(getString(R.string.rest_rooms))
//        tasksList.add(getString(R.string.general_area))
        tasksList = resources.getStringArray(R.array.task_tables).toList() as ArrayList<String>

        listOfTimingsColumns = resources.getStringArray(R.array.timings).toList() as ArrayList<String>
        binding.etSelectDateInput.setText(Utils.getCurrentDate())
//        binding.etSelectTaskInput.setText(getString(R.string.pantry))

        binding.etSelectTaskInput.setOnClickListener {
            openDialog(getString(R.string.select_task),tasksList)
        }
        binding.etSelectDateInput.setOnClickListener {
          Utils.setCalender(requireActivity()) {
              binding.etSelectDateInput.setText(it)
          }

        }
        binding.download.setOnClickListener {

             selectedDate= binding.etSelectDateInput.text.toString()
            var selectedTask = binding.etSelectTaskInput.text.toString()
//            var selectedTable = getColumName(selectedTask)
            println("selectedTask::"+selectedTask)
            if( selectedDate==""){
                   Toast.makeText(requireActivity(),"Please select date",Toast.LENGTH_SHORT).show()
            }else if(selectedTask==""){
                Toast.makeText(requireActivity(),"Please select task",Toast.LENGTH_SHORT).show()

            }else {
                when (selectedTask) {
                    getString(R.string.pantry) -> viewModel.getAllPantryTasksbydate(selectedDate)
                    getString(R.string.rest_rooms) -> requestToRestRoomData()
                }
            }
        }
    }
    fun requestToRestRoomData(){
        viewModel.getAllRestRoomTasksDateWise(selectedDate)
    }
    private fun requestViewModel(){
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]
        viewModel.getAllRestRoomColumnData()
        viewModel.getAllPantryColumnns()


    }
    private fun Observers(){
        viewModel.getAllPantryTasksByDateWise().observe(viewLifecycleOwner){
            listOfPantryColumns.clear()
            println("getAllPantryTasksByDateWise:: "+it)
       /*   var isExcelGenerated= ExcelUtils.exportPantryDataIntoWorkbook(
              context,
              Constants.PANTRY_EXCEL_FILE_NAME + selectedDate + Constants.EXTENSION,
              selectedDate,
              it
          )*/
            var  restRoomTasks= it.groupBy { it.created_time }
            var secondObject:Map<String,Any> = HashMap<String,Any>()
            if(restRoomTasks!=null &&restRoomTasks.size>0){
               var pantryFirstElement:List<PantryTasks> = restRoomTasks.values.toList()[0]
                listOfPantryColumns.add("Assign To")
               pantryFirstElement.forEach {
//                   if(it.task_name!="id") {
                       listOfPantryColumns.add(it.task_name)
//                   }
               }
            }
            var fileName=Constants.PANTRY_EXCEL_FILE_NAME +"($selectedDate)"
            val existingStringInShared=SharedPreferencesManager.getString(selectedDate+"_"+getString(R.string.pantry),"")
            if(existingStringInShared!=""){
                fileName = existingStringInShared
            }
            println("listOfPantryColumns:: "+listOfPantryColumns.size)

            downloadExcel(fileName,restRoomTasks,secondObject,getString(R.string.pantry))

        }
        viewModel.getAllRestroomColumns().observe(viewLifecycleOwner) { it ->
            listOfRestRoomColumns = it as ArrayList<String>
        }
        /*viewModel.getAllPantryColumnsRequest().observe(viewLifecycleOwner) { it ->
            listOfPantryColumns = it as ArrayList<String>
        }*/
        viewModel.getAllRestRoomTasksByDateWise().observe(viewLifecycleOwner){
            maleRestRoomTasks = it as ArrayList<RestRoomTasks>
           /* it.forEach {it1->
                var hashMap = HashMap<String,RestRoomTasks>()
                hashMap[it1.created_time] = it1
                restRoomTasks.add(hashMap)
            }*/
            viewModel.sendRequestToAllFemaleRestRoomData(selectedDate)


        }
        viewModel.getFemaleRestroomTasksByDateTime().observe(viewLifecycleOwner){
            var femaleRestRoomTasksmaleRestRoomTasks = it as ArrayList<FemaleRestRoomTasks>

            var restRoomTasks= maleRestRoomTasks.groupBy { it.created_time }
            var femaleRestRoomTasks= femaleRestRoomTasksmaleRestRoomTasks.groupBy { it.created_time }

           /* var isExcelGenerated= ExcelUtils.exportRestRoomDataIntoWorkbook(
                context,
                Constants.RESTROOM_EXCEL_FILE_NAME + "($selectedDate)5656" + Constants.EXTENSION,
                selectedDate, restRoomTasks,femaleRestRoomTasks, listOfRestRoomColumns, listOfTimingsColumns
            )*/
//            var isExcelGenerated=  ExcelUtils.exportPantryDataIntoWorkbook(context, Constants.PANTRY_EXCEL_FILE_NAME+selectedDate+Constants.EXTENSION,selectedDate,it)

          /*  if(isExcelGenerated){
                Utils.showAlertDialog(requireActivity(), "Excel Download Successful")
            }else{
                Utils.showAlertDialog(requireActivity(), "Excel Download Successful")

            }*/
            if(restRoomTasks!=null &&restRoomTasks.size>0){
                var restroomFirstElement:List<RestRoomTasks> = restRoomTasks.values.toList()[0]
                listOfPantryColumns.add("Assign To")
                restroomFirstElement.forEach {
//                   if(it.task_name!="id") {
                    listOfPantryColumns.add(it.task_name)
//                   }
                }
            }

        var fileName=Constants.RESTROOM_EXCEL_FILE_NAME +"($selectedDate)"
        val existingStringInShared=SharedPreferencesManager.getString(selectedDate+"_"+getString(R.string.rest_rooms),"")
        if(existingStringInShared!=""){
            fileName = existingStringInShared
        }
        println("listOfPantryColumns:: "+listOfPantryColumns.size)

        downloadExcel(fileName,restRoomTasks,femaleRestRoomTasks,getString(R.string.rest_rooms))
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun downloadExcel(fileName:String, anyObj:Map<String, Any>,femaleAnyObj:Map<String, Any>,category:String){
        var isExcelGenerated= ExcelUtils.exportPantryDataIntoWorkbook(context, fileName + Constants.EXTENSION, selectedDate, anyObj,femaleAnyObj, listOfPantryColumns, listOfTimingsColumns,category)

            if(isExcelGenerated){

                SharedPreferencesManager.saveString(selectedDate+"_"+category,fileName)
                Utils.showAlertDialog(requireActivity(),"Excel Download Successful")

            }else{
                val currentDateTime: java.util.Date = java.util.Date()

                val currentTimestamp: Long = currentDateTime.time
                val fileName = "$currentTimestamp"+"_"+selectedDate+"_"+category
                downloadExcel(fileName,anyObj,femaleAnyObj,category)
            }

    }
    private fun openDialog(dropFrom: String, listArr: ArrayList<String>) {

        var layoutDailogListViewBinding = LayoutDailogListViewBinding.inflate(layoutInflater)
        val shareAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        shareAlertBuilder.setView(layoutDailogListViewBinding.root)
        layoutDailogListViewBinding.tvHeader.text = dropFrom

        val alertDialog: AlertDialog = shareAlertBuilder.create()
        layoutDailogListViewBinding.listView.adapter = CommanAdapter(requireActivity(), listArr)
        layoutDailogListViewBinding.listView.setOnItemClickListener { parent, view, position, id ->
            if (dropFrom == getString(R.string.select_task)) {
                binding.etSelectTaskInput.setText(listArr[position])
            }
          /*  else if(dropFrom == getString(R.string.select_date)){
                binding.etSelectDateInput.setText(listArr[position])
            }*/
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

  /*  private fun getColumName(selectedTask:String):String{
        var value=""
        when(selectedTask){
            getString(R.string.pantry) -> value = Constants.pantry_tasks_table
            getString(R.string.rest_rooms) -> value = Constants.restroom_tasks_table
        }
       return value
    }*/

}