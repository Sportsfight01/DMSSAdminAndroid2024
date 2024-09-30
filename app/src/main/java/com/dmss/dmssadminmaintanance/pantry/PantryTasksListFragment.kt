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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
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
import com.dmss.dmssadminmaintanance.sidemenu.AssigningToPersonFragment
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
        binding.filterLayout.selectedDate.setText(Utils.getCurrentDate())

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
           Utils. setCalender(requireActivity()){
               binding.filterLayout.selectedDate.setText(it)
               /* viewModel.getAllPantryTasksbydate1(binding.filterLayout.selectedDate.text.toString(),true)
                listArr.clear()
                checkBoxRecycleviewAdapter.loadItems(listArr)*/


               refreshList()
           }
        }

        binding.rvPantry.adapter = checkBoxRecycleviewAdapter

//        viewModel.getAllNotAssignPantryTasksbydateRequest(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),true)

        binding.filterLayout.ctSelectAll.setOnClickListener(View.OnClickListener { v ->
            selectedItems.clear()
            var newAllSelectedItemsArr = ArrayList<CheckBoxModel>()

            if(listArr.size>0) {
                (v as CheckedTextView).toggle()
//                listArr.clear()
//                println("coulumnNames:: " + coulumnNames.size+"assigendList:: "+assigendList.size)
                    println("listArr" + listArr.size)
//                }
                listArr.forEach {
                    var isChecked =false
                    if (v.isChecked) {
                        isChecked = true
                        selectedItems.add(CheckBoxModel(0, true, it.text))

                    }
                    var chr = CheckBoxModel().apply {
                        position = 0
                        checked = isChecked
                        text = it.text
                    }
                    newAllSelectedItemsArr.add(chr)
                }
                println("newAllSelectedItemsArr:: "+newAllSelectedItemsArr)
                listArr = newAllSelectedItemsArr
                checkBoxRecycleviewAdapter.loadItems(listArr)
            }else{
//                Toast.makeText(requireActivity(),"No items",Toast.LENGTH_SHORT).show()
            }

        })
        binding.filterLayout.submit.setOnClickListener {
            if (selectedItems.size > 0) {
                if (assigendToPersonList.size > 0) {

                    openAssignedPersonDialog(assigendToPersonList)
                } else {
                    Utils.confirmationAlertAlertDialog(
                        requireActivity(),
                        getString(R.string.no_assign_person)
                    ) {
                        navigateTo(R.id.assign_person_fragment)
                    }
                }
            }
            else{
                Utils.showAlertDialog(requireActivity(),getString(R.string.please_select_at_least_one_item))
            }

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
        viewModel.getAllPantryTasksByDate().observe(viewLifecycleOwner){
            println("listTaskData:: getAllPantryTasksByDate "+it.size)
            if(it.isEmpty()) {

                viewModel.getAllPantryColumnsRequest()
            }

        }
        viewModel.getAllPantryColumnns().observe(viewLifecycleOwner) { it ->
            println("listTaskData:: getAllPantryColumnns"+it.size)

            coulumnNames = it
            pantryListDataArr.clear()
            it.forEach {
                if (!assigendList.contains(it) && it!="id")
                    listArr.add(CheckBoxModel(0, false, it))

            }

            checkBoxRecycleviewAdapter.loadItems(listArr)


        }
        viewModel.getAllNotAssignedPantryTasksDataBydate().observe(viewLifecycleOwner){
            println("listTaskData:: getAllNotAssignedPantryTasksDataBydate "+it.size)
            assigendList.clear()
            if(it.isNotEmpty()) {
                it.forEach {
                    if ( it.task_name!="id") {

                        listArr.add(CheckBoxModel(0, false, it.task_name))

                        assigendList.add(it.task_name)
                    }
                }
            }else{
                viewModel.getAllPantryTasksbydate1(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),true)

//                viewModel.getAllPantryColumnsRequest()

            }

            checkBoxRecycleviewAdapter.loadItems(listArr)
        }
    }

    private fun refreshList(){
        listArr.clear()
//        checkBoxRecycleviewAdapter.loadItems(listArr)

//        viewModel.getAllPantryTasksbydate1(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),true)
      var selectedTime = binding.filterLayout.selectTime.text.toString()
      var selectedDate = binding.filterLayout.selectedDate.text.toString()
      println("selectedTime:: "+selectedTime+"  selectedDate:: "+selectedDate)
        viewModel.getAllNotAssignPantryTasksbydateRequest(selectedDate,selectedTime,false)


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
        if(assigendList.size==0) {
            var selectedext = ""
            coulumnNames.forEach { it1 ->
                var isCompleted = false
                selectedItems.forEach { it2 ->
                    selectedext = selectedext + "," + it2.text
                    if (it2.text == it1) {
                        isCompleted = true
                    }
                }
                pantryListDataArr.add(
                    PantryTasks(
                        it1,
                        selectedDate.toString(),
                        selectedTime.toString(),
                        selectedItem,
                        isCompleted,
                        false
                    )
                )
            }
            val listlastSize = pantryListDataArr.lastIndex


            pantryListDataArr.forEachIndexed { index, element ->
//                println("element:: "+element)
                    viewModel.insertPantryTasks(element)

                if (index == listlastSize) {
                    Thread.sleep(500)
                    selectedItems.clear()
                    refreshList()
                    Toast.makeText(context, getString(R.string.tasks_assigned_success), Toast.LENGTH_SHORT).show()

                }
            }
        }else {
            if (assigendList.size > 0) {
                val listlastSize = selectedItems.lastIndex

                selectedItems.forEachIndexed { index,it2 ->
                val pantryTask = PantryTasks().apply {
                    task_name = it2.text
                    created_date = binding.filterLayout.selectedDate.text.toString()
                    created_time = binding.filterLayout.selectTime.text.toString()
                    AssignedTo = selectedItem
                    isAssigned = true
                    isCompleted = false
                }
                viewModel.updatePantryTasks(pantryTask)
                    if (index == listlastSize) {
                        Thread.sleep(500)
                        selectedItems.clear()
                        refreshList()
                        Toast.makeText(context, getString(R.string.tasks_assigned_success), Toast.LENGTH_SHORT).show()

                    }
            }
        }
        }
    }
    private fun navigateTo(resId: Int) {
        var  navController = requireActivity().findNavController(R.id.nav_host_fragment_content_dashboard_new)

        if (checkCurrentFragment(resId).not())
            navController.navigate(resId)
    }
    private fun checkCurrentFragment(id : Int): Boolean{
        val navController =
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_dashboard_new)
        return navController.currentDestination?.id == id
    }
}