package com.dmss.dmssadminmaintanance.pantry

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.databinding.FragmentPantryTasksListBinding
import com.dmss.dmssadminmaintanance.databinding.LayoutDailogListViewBinding
import com.dmss.dmssadminmaintanance.db.PantryTasks
import com.dmss.dmssadminmaintanance.db.RestRoomTasks
import com.dmss.dmssadminmaintanance.model.CheckBoxModel
import com.dmss.dmssadminmaintanance.model.TaskData
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.CommanAdapter
import com.dmss.dmssadminmaintanance.pantry.adapter.PantryTasksAdapter
import com.dmss.dmssadminmaintanance.xcelsheet.Constants
import com.dmss.dmssadminmaintanance.xcelsheet.ExcelUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class PantryTasksListFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding : FragmentPantryTasksListBinding
    private lateinit var viewModel: MaintainanceViewModel
    var listArr: MutableList<CheckBoxModel> = ArrayList<CheckBoxModel>()
    var coulumnNames: List<String> = ArrayList<String>()
    var pantryListDataArr: ArrayList<PantryTasks> = ArrayList<PantryTasks>()
    var selectedItems: ArrayList<CheckBoxModel> = ArrayList<CheckBoxModel>()
    var listTaskData = ArrayList<PantryTasks>()
    var assigendList = ArrayList<String>()
    var assigendToPersonList = ArrayList<String>()

    lateinit var checkBoxRecycleviewAdapter :PantryTasksAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPantryTasksListBinding.inflate(inflater, container, false)
        requestViewModel()
        Observers()
        initView()

        return binding.root
    }
    fun initView() {
        checkBoxRecycleviewAdapter = PantryTasksAdapter(getString(R.string.pantry)) { it ->
            selectedItems = it as ArrayList<CheckBoxModel>
        }
        val layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.rvPantry.layoutManager = layoutManager

        var currentHour=Utils.getCurrentHour()
        binding.filterLayout.selectTime.setText(currentHour)
        binding.filterLayout.submit.setText(getString(R.string.assign))

        refreshList()
        binding.filterLayout.selectTime.setOnClickListener {
            PopupMenu(requireActivity(), binding.filterLayout.selectTime).apply {
                menuInflater.inflate(R.menu.spinner_items, menu)
                setOnMenuItemClickListener { item ->
                    binding.filterLayout.selectTime.setText(item.title)
                    refreshList()

                    true
                }
            }. show()
        }
        binding.filterLayout.llTimer.visibility=View.VISIBLE

        binding.filterLayout.ciCalender.setOnClickListener {
            setCalender()
        }

        binding.rvPantry.adapter = checkBoxRecycleviewAdapter
        binding.filterLayout.selectedDate.setText(Utils.getCurrentDate())

        viewModel.getAllPantryTasksbydate1(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),true)
        viewModel.getAllPantryTasksByDate().observe(viewLifecycleOwner){
            println("listTaskData:: "+it.size)
            assigendList.clear()
            it.forEach {

                assigendList.add(it.task_name)
            }

            viewModel.getAllPantryColumnsRequest()

        }
        viewModel.getAllPantryColumnns().observe(viewLifecycleOwner) { it ->
            println("listTaskData:: "+it.size)

            coulumnNames = it
            pantryListDataArr.clear()
            it.forEach {
                if (!assigendList.contains(it) && it!="id")
                        listArr.add(CheckBoxModel(0, false, it))

            }
            println("coulumnNames11:: " + coulumnNames.size)

            checkBoxRecycleviewAdapter.loadItems(listArr)


        }
        binding.filterLayout.ctSelectAll.setOnClickListener(View.OnClickListener { v ->
            (v as CheckedTextView).toggle()
            listArr.clear()
            println("coulumnNames:: " + coulumnNames.size)
            coulumnNames.forEach {
                if (it != "isCompleted" && it != "id" && it != "create_date")
                    if (v.isChecked)
                        listArr.add(CheckBoxModel(0, true, it))
                    else
                        listArr.add(CheckBoxModel(0, false, it))

            }
            println("listArr" + listArr.size)
            checkBoxRecycleviewAdapter.loadItems(listArr)

        })
        binding.filterLayout.submit.setOnClickListener {
            openAssignedPersonDialog(assigendToPersonList)
        }

    }
    fun requestViewModel(){
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]
        viewModel.getAssignedPersonRequest()

    }
    fun Observers(){
        viewModel.getAssignTaskPersons().observe(viewLifecycleOwner){
            println("getAssignTaskPersons::"+it)
            it.forEach {
                assigendToPersonList.add(it.name)
            }

        }
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
                binding.filterLayout.selectedDate.setText(dat)
               /* viewModel.getAllPantryTasksbydate1(binding.filterLayout.selectedDate.text.toString(),true)
                listArr.clear()
                checkBoxRecycleviewAdapter.loadItems(listArr)*/


                refreshList()
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
    private fun refreshList(){
        listArr.clear()
        checkBoxRecycleviewAdapter.loadItems(listArr)

        viewModel.getAllPantryTasksbydate1(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),true)
    }
    private fun openAssignedPersonDialog( listArr: ArrayList<String>) {

        var layoutDailogListViewBinding = LayoutDailogListViewBinding.inflate(layoutInflater)
        val shareAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        shareAlertBuilder.setView(layoutDailogListViewBinding.root)
        layoutDailogListViewBinding.tvHeader.text = "Assigned To"

        val alertDialog: AlertDialog = shareAlertBuilder.create()
        layoutDailogListViewBinding.listView.adapter = CommanAdapter(requireActivity(), listArr)
        layoutDailogListViewBinding.listView.setOnItemClickListener { parent, view, position, id ->
            var selectedItem=listArr[position]
            Utils.confirmationAlertAlertDialog(requireActivity(),"Are you sure! Do you want to assign task to $selectedItem ?"){
                if(it){
                    updateData(selectedItem)
                }
            }
            /*  else if(dropFrom == getString(R.string.select_date)){
                  binding.etSelectDateInput.setText(listArr[position])
              }*/
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    fun updateData(selectedItem:String){
        var selectedDate=binding.filterLayout.selectedDate.text
        var selectedTime=binding.filterLayout.selectTime.text

        println("selectedDate:: "+selectedDate)
        var selectedext = ""
        coulumnNames.forEach {it1->
            var isCompleted=false
            selectedItems.forEach { it2->
                selectedext = selectedext + "," + it2.text
                if(it2.text==it1){
                    isCompleted =true
                }
            }
            pantryListDataArr.add( PantryTasks(it1,selectedDate.toString(),selectedTime.toString(),selectedItem,isCompleted,false) )
        }
        val listlastSize=pantryListDataArr.lastIndex

        pantryListDataArr.forEachIndexed{ index, element ->
//                println("element:: "+element)
            viewModel.insertPantryTasks(element)
            if(index==listlastSize){
                Thread.sleep(500)
                refreshList()
                Toast.makeText(context,"Tasks Assigned Success..", Toast.LENGTH_SHORT).show()

            }
        }
    }
}