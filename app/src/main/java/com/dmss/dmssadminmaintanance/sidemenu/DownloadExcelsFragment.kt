package com.dmss.dmssadminmaintanance.sidemenu

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.databinding.FragmentDownloadExcelsBinding
import com.dmss.dmssadminmaintanance.databinding.LayoutDailogListViewBinding
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.CommanAdapter
import com.dmss.dmssadminmaintanance.xcelsheet.Constants
import com.dmss.dmssadminmaintanance.xcelsheet.ExcelUtils
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
    var listOfRestRoomTimingsColumns = ArrayList<String>()

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

        tasksList.add(getString(R.string.cabins))
        tasksList.add(getString(R.string.games_room))
        tasksList.add(getString(R.string.work_station))
        tasksList.add(getString(R.string.pantry))
        tasksList.add(getString(R.string.rest_rooms))
        tasksList.add(getString(R.string.general_area))
        listOfRestRoomTimingsColumns.add("7:00");
        listOfRestRoomTimingsColumns.add("8:00");
        listOfRestRoomTimingsColumns.add("9:00");
        listOfRestRoomTimingsColumns.add("10:00");
        listOfRestRoomTimingsColumns.add("11:00");
        listOfRestRoomTimingsColumns.add("12:00");
        listOfRestRoomTimingsColumns.add("13:00");
        listOfRestRoomTimingsColumns.add("14:00");
        listOfRestRoomTimingsColumns.add("15:00");
        listOfRestRoomTimingsColumns.add("16:00");
        listOfRestRoomTimingsColumns.add("17:00");
        listOfRestRoomTimingsColumns.add("18:00");
        listOfRestRoomTimingsColumns.add("19:00");
        listOfRestRoomTimingsColumns.add("20:00");


        binding.etSelectTaskInput.setOnClickListener {
            println("etSelectTask:: ")
            openDialog(getString(R.string.select_task),tasksList)
        }
        binding.etSelectDateInput.setOnClickListener {
           setCalender()

        }
        binding.download.setOnClickListener {

             selectedDate= binding.etSelectDateInput.text.toString()
            var selectedTask = binding.etSelectTaskInput.text.toString()
//            var selectedTable = getColumName(selectedTask)
            when(selectedTask){
                getString(R.string.pantry) ->  viewModel.getAllPantryTasksbydate(selectedDate)
                getString(R.string.rest_rooms) -> viewModel.getAllRestRoomTasksDateWise(selectedDate)
            }

        }
    }
    private fun requestViewModel(){
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]
        viewModel.getAllRestRoomColumnData()

    }
    private fun Observers(){
        viewModel.getAllPantryTasksByDateWise().observe(viewLifecycleOwner){

            println("getAllPantryTasksByDateWise:: "+it)
          var isExcelGenerated= ExcelUtils.exportPantryDataIntoWorkbook(
              context,
              Constants.PANTRY_EXCEL_FILE_NAME + selectedDate + Constants.EXTENSION,
              selectedDate,
              it
          )

            if(isExcelGenerated){
                dowloadAlert("Excel Download Successful")
            }else{
                dowloadAlert("Unable to Download Excel. Please try again..")

            }
        }
        viewModel.getAllRestroomColumns().observe(viewLifecycleOwner) { it ->
            listOfRestRoomColumns = it as ArrayList<String>
        }
        viewModel.getAllRestRoomTasksByDateWise().observe(viewLifecycleOwner){
           var restRoomTasks= it.groupBy { it.created_time }
           /* it.forEach {it1->
                var hashMap = HashMap<String,RestRoomTasks>()
                hashMap[it1.created_time] = it1
                restRoomTasks.add(hashMap)
            }*/
            println("getAllRestRoomTasksByDateWise:: "+restRoomTasks)

            var isExcelGenerated= ExcelUtils.exportRestRoomDataIntoWorkbook(
                context,
                Constants.RESTROOM_EXCEL_FILE_NAME + "11($selectedDate)" + Constants.EXTENSION,
                selectedDate, restRoomTasks, listOfRestRoomColumns, listOfRestRoomTimingsColumns
            )
//            var isExcelGenerated=  ExcelUtils.exportPantryDataIntoWorkbook(context, Constants.PANTRY_EXCEL_FILE_NAME+selectedDate+Constants.EXTENSION,selectedDate,it)

            if(isExcelGenerated){
                Utils.showAlertDialog(requireActivity(), "Excel Download Successful")
            }else{
                Utils.showAlertDialog(requireActivity(), "Excel Download Successful")

            }
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

    private fun setCalender(){
        val c = Calendar.getInstance()

        // on below line we are getting
        // our day, month and year.
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // on below line we are creating a
        // variable for date picker dialog.
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                binding.etSelectDateInput.setText(dat)
            },
            // on below line we are passing year, month
            // and day for the selected date in our date picker.
            year,
            month,
            day
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
    }
    private fun getColumName(selectedTask:String):String{
        var value=""
        when(selectedTask){
            getString(R.string.pantry) -> value = Constants.pantry_tasks_table
            getString(R.string.rest_rooms) -> value = Constants.restroom_tasks_table
        }
       return value
    }

    private fun dowloadAlert(title:String)
    {

            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage(title)
            builder.setTitle(getString(R.string.dmss))
            builder.setCancelable(false)
            builder.setPositiveButton(
                Html.fromHtml("<font color=" + this.resources.getColor(R.color.black) + ">OK</font>"),
                DialogInterface.OnClickListener {
                        dialog: DialogInterface, _: Int -> dialog.dismiss()
                    dialog.dismiss()


                })
           /* builder.setNegativeButton(
                Html.fromHtml("<font color=" + this.resources.getColor(R.color.black) + ">No</font>"),
                DialogInterface.OnClickListener {
                        dialog: DialogInterface, _: Int -> dialog.dismiss()
                    dialog.dismiss()
                })*/
            val alertDialog = builder.create()
            // Show the Alert Dialog box
            alertDialog.show()
        }

}