package com.dmss.dmssadminmaintanance.restroom

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.dmss.dmssadminmaintanance.db.FemaleRestRoomTasks
import com.dmss.dmssadminmaintanance.db.PantryTasks
import com.dmss.dmssadminmaintanance.db.RestRoomTasks
import com.dmss.dmssadminmaintanance.model.CheckBoxModel
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.CommanAdapter
import com.dmss.dmssadminmaintanance.pantry.adapter.PantryTasksAdapter
import com.dmss.dmssadminmaintanance.sidemenu.AssigningToPersonFragment
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RestRooTasksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestRooTasksFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding : FragmentPantryTasksListBinding
    private lateinit var viewModel: MaintainanceViewModel
    var listArr: MutableList<CheckBoxModel> = ArrayList<CheckBoxModel>()
    var coulumnNames: List<String> = ArrayList<String>()
    var pantryListDataArr: ArrayList<RestRoomTasks> = ArrayList<RestRoomTasks>()
    var felameListDataArr: ArrayList<FemaleRestRoomTasks> = ArrayList<FemaleRestRoomTasks>()

    var selectedItems: ArrayList<CheckBoxModel> = ArrayList<CheckBoxModel>()
    var listTaskData = ArrayList<PantryTasks>()
    lateinit var checkBoxRecycleviewAdapter:PantryTasksAdapter
    var assigendList = ArrayList<String>()
    var assigendToPersonList = ArrayList<String>()

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
    @SuppressLint("SuspiciousIndentation")
    fun initView() {
        var currentHour=Utils.getCurrentHour()
        binding.filterLayout.selectTime.setText(currentHour)
        binding.filterLayout.llTimer.visibility=View.VISIBLE

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
        binding.filterLayout.ciCalender.setOnClickListener {
            Utils.setCalender(requireActivity()) {
                binding.filterLayout.selectedDate.setText(it)
                refreshList()
            }
        }
        checkBoxRecycleviewAdapter = PantryTasksAdapter(getString(R.string.rest_rooms)) { it ->
            println("FilteredList:: " + it.size)
            selectedItems = it as ArrayList<CheckBoxModel>

        }
        val layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.rvPantry.layoutManager = layoutManager

        binding.rvPantry.adapter = checkBoxRecycleviewAdapter
        binding.filterLayout.selectedDate.text = Utils.getCurrentDate()
        refreshList()

        binding.filterLayout.ctSelectAll.setOnClickListener(View.OnClickListener { v ->
//            (v as CheckedTextView).toggle()
           /* listArr.clear()
            coulumnNames.forEach {
                selectedItems.add(CheckBoxModel(0, true, it))

                if (it != "isCompleted" && it != "id" && it != "create_date")
                    if (v.isChecked)
                        listArr.add(CheckBoxModel(0, true, it))
                    else
                        listArr.add(CheckBoxModel(0, false, it))

            }
            println("listArr" + listArr.size)
            checkBoxRecycleviewAdapter.loadItems(listArr)*/
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
            }

        })
        binding.filterLayout.submit.setOnClickListener {
            /*   val formatter = SimpleDateFormat("yyyy-MM-dd")
               val date = Date()
               val current = formatter.format(date)*/
            if(selectedItems.size>0) {
                if (assigendToPersonList.size > 0) {
                    pantryListDataArr.clear()
                    felameListDataArr.clear()
                    openAssignedPersonDialog(assigendToPersonList)
                } else {
                    Utils.confirmationAlertAlertDialog(
                        requireActivity(),
                        getString(R.string.no_assign_person)
                    ) {
                        navigateTo(R.id.assign_person_fragment)
                    }
                }
            }else{
                Utils.showAlertDialog(requireActivity(),getString(R.string.please_select_at_least_one_item))

            }
//       viewModel.allPantryTasksDataBydate?.observe(viewLifecycleOwner){
//           println("List of pantryTaksEntityData:: $it")
//       }
        }

    }
    private fun openAssignedPersonDialog( listArr: ArrayList<String>) {

        var layoutDailogListViewBinding = LayoutDailogListViewBinding.inflate(layoutInflater)
        val shareAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        shareAlertBuilder.setView(layoutDailogListViewBinding.root)
        layoutDailogListViewBinding.tvHeader.text = "Assigned To"
        var selectedDate=binding.filterLayout.selectedDate.text
        var selectedTime=binding.filterLayout.selectTime.text
        binding.filterLayout.submit.text = getString(R.string.assign)

        val alertDialog: AlertDialog = shareAlertBuilder.create()
        layoutDailogListViewBinding.listView.adapter = CommanAdapter(requireActivity(), listArr)
        layoutDailogListViewBinding.listView.setOnItemClickListener { parent, view, position, id ->
//          Utils.showProgressDialog(requireActivity())
          /*  var selectedext = ""
            coulumnNames.forEach {it1->
                var isCompleted=false
                selectedItems.forEach { it2->
                    selectedext = selectedext + "," + it2.text
                    if(it2.text==it1){
                        isCompleted =true
                    }
                }
                if(Utils.selectedGender==getString(R.string.male)) {
                    pantryListDataArr.add(
                        RestRoomTasks(
                            it1,
                            selectedDate.toString(),
                            selectedTime.toString(),
                            listArr[position],
                            isCompleted,
                            false
                        )
                    )
                }else{
                    felameListDataArr.add(
                        FemaleRestRoomTasks(
                            it1,
                            selectedDate.toString(),
                            selectedTime.toString(),
                            listArr[position],
                            isCompleted,
                            false
                        )
                    )
                }
            }*/
            var selectedItem=  listArr[position]
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
        val listlastSize = pantryListDataArr.lastIndex
        val femalelastSize = felameListDataArr.lastIndex
        if(Utils.selectedGender==getString(R.string.male)) {

            if (assigendList.size== 0) {
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
                        RestRoomTasks(
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
                    println("insertRestRoomTasks:: "+element)

                    viewModel.insertRestRoomTasks(element)

                    if (index == listlastSize) {
//                    Utils.dismissProgressDialog()

                        Thread.sleep(500)
                        refreshList()
                        selectedItems.clear()
                        Toast.makeText(context, "Tasks Assigned Success..", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
            }else{
                if (assigendList.size > 0) {
                    val listlastSize = selectedItems.lastIndex

                    selectedItems.forEachIndexed { index,it2 ->
                        val restRoomTasks = RestRoomTasks().apply {
                            task_name = it2.text
                            created_date = binding.filterLayout.selectedDate.text.toString()
                            created_time = binding.filterLayout.selectTime.text.toString()
                            AssignedTo = selectedItem
                            isAssigned = true
                            isCompleted = false
                        }
                        println("updateRestRoomTasks:: "+restRoomTasks)
                        viewModel.updateRestRoomTasks(restRoomTasks)
                        if (index == listlastSize) {
                            Thread.sleep(500)
                            refreshList()
                            selectedItems.clear()
                            Toast.makeText(context, getString(R.string.tasks_assigned_success), Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
        }
        else{
           /* felameListDataArr.forEachIndexed { index, element ->
                println("element:: "+element)
                viewModel.insertFemaleRestRoomTasks(element)

                if (index == femalelastSize) {
//                    Utils.dismissProgressDialog()

                    Thread.sleep(500)
                    refreshList()

                    Toast.makeText(context, "Tasks Assigned Success..", Toast.LENGTH_SHORT)
                        .show()

                }
            }*/


            if (assigendList.size== 0) {
                var selectedext = ""

                coulumnNames.forEach { it1 ->
                    var isCompleted = false
                    selectedItems.forEach { it2 ->
                        selectedext = selectedext + "," + it2.text
                        if (it2.text == it1) {
                            isCompleted = true
                        }
                    }
                    felameListDataArr.add(
                        FemaleRestRoomTasks(
                            it1,
                            selectedDate.toString(),
                            selectedTime.toString(),
                            selectedItem,
                            isCompleted,
                            false
                        )
                    )
                }
                val listlastSize = felameListDataArr.lastIndex

                felameListDataArr.forEachIndexed { index, element ->
//                println("element:: "+element)
                    println("insertRestRoomTasks:: "+element)

                    viewModel.insertFemaleRestRoomTasks(element)

                    if (index == listlastSize) {
//                    Utils.dismissProgressDialog()

                        Thread.sleep(500)
                        refreshList()
                        Toast.makeText(context, "Tasks Assigned Success..", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
            }else{
                if (assigendList.size > 0) {
                    val listlastSize = selectedItems.lastIndex

                    selectedItems.forEachIndexed { index,it2 ->
                        val restRoomTasks = FemaleRestRoomTasks().apply {
                            task_name = it2.text
                            created_date = binding.filterLayout.selectedDate.text.toString()
                            created_time = binding.filterLayout.selectTime.text.toString()
                            AssignedTo = selectedItem
                            isAssigned = true
                            isCompleted = false
                        }
                        println("updateRestRoomTasks:: "+restRoomTasks)
                        viewModel.updateFemaleRestRoomTasks(restRoomTasks)
                        if (index == listlastSize) {
                            Thread.sleep(500)
                            refreshList()
                            Toast.makeText(context, getString(R.string.tasks_assigned_success), Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
        }
    }
    fun requestViewModel(){
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]
        viewModel.getAssignedPersonRequest()

    }
    fun Observers(){
        viewModel.getAssignTaskPersons().observe(viewLifecycleOwner){
            it.forEach {
                assigendToPersonList.add(it.name)
            }

        }
        viewModel.getAllNotAssignedMaleRestRoomTasksDataBydate().observe(viewLifecycleOwner){
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
                viewModel.getAllRestRoomTasks(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),true)

//                viewModel.getAllPantryColumnsRequest()

            }

            checkBoxRecycleviewAdapter.loadItems(listArr)
        }
        viewModel.getAllNotAssignedFeMaleRestRoomTasksDataBydate().observe(viewLifecycleOwner){
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
                viewModel.sendRequestToFemaleRestRoomAssignedTasks(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),true)

//                viewModel.getAllPantryColumnsRequest()

            }

            checkBoxRecycleviewAdapter.loadItems(listArr)
        }
        viewModel.getAllRestRoomTasksByDate().observe(viewLifecycleOwner){
          /*  assigendList.clear()
            it.forEach {
                assigendList.add(it.task_name)
            }

            viewModel.getAllRestroomColumns()*/
            if(it.isEmpty()) {

                viewModel.getAllRestroomColumns()
            }

        }
        viewModel.getFemaleRestroomTasksByDateTime().observe(viewLifecycleOwner){
            assigendList.clear()
            it.forEach {
                assigendList.add(it.task_name)
            }

            viewModel.getAllRestroomColumns()

        }
        viewModel.getAllRestRoomColumnData().observe(viewLifecycleOwner) { it ->
            coulumnNames = it
            pantryListDataArr.clear()
            it.forEach {
                if (!assigendList.contains(it) && it!="id")
                    listArr.add(CheckBoxModel(0, false, it))
            }

            checkBoxRecycleviewAdapter.loadItems(listArr)


        }
    }
    private fun refreshList(){
        listArr.clear()
        checkBoxRecycleviewAdapter.loadItems(listArr)
        var selectedTime = binding.filterLayout.selectTime.text.toString()
        var selectedDate = binding.filterLayout.selectedDate.text.toString()
        if(Utils.selectedGender==getString(R.string.male)) {
         /*   viewModel.getAllRestRoomTasks(
                binding.filterLayout.selectedDate.text.toString(),
                binding.filterLayout.selectTime.text.toString(),
                true
            )*/
            viewModel.getAllNotAssignMaleRestRoomTasksbydateRequest(selectedDate,selectedTime,false)

        }else{
            viewModel.getAllNotAssignFemaleRestRoomTasksbydateRequest(selectedDate,selectedTime,false)

            /* viewModel.sendRequestToFemaleRestRoomAssignedTasks( binding.filterLayout.selectedDate.text.toString(),
                 binding.filterLayout.selectTime.text.toString(),
                 true)*/
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