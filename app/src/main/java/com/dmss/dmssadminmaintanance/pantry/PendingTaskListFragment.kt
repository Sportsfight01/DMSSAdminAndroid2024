package com.dmss.dmssadminmaintanance.pantry

import android.app.DatePickerDialog
import android.os.Bundle
import android.provider.ContactsContract.Contacts
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.databinding.FragmentPendingTaskListBinding
import com.dmss.dmssadminmaintanance.db.PantryTasks
import com.dmss.dmssadminmaintanance.model.CheckBoxModel
import com.dmss.dmssadminmaintanance.model.TaskData
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.PantryTasksAdapter
import com.dmss.dmssadminmaintanance.xcelsheet.Constants
import com.dmss.dmssadminmaintanance.xcelsheet.ExcelUtils
import com.dmss.dmssadminmaintanance.xcelsheet.GenerateExcelSheet
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PendingTaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PendingTaskListFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentPendingTaskListBinding
    private lateinit var viewModel: MaintainanceViewModel
    var listArr: MutableList<CheckBoxModel> = ArrayList<CheckBoxModel>()
    var selectedItems: MutableList<CheckBoxModel> = ArrayList<CheckBoxModel>()
    lateinit var checkBoxRecycleviewAdapter :PantryTasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingTaskListBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }
    private fun initView() {
        var listTaskData = ArrayList<TaskData>()
        binding.filterLayout.submit.setText(getString(R.string.complete))

        binding.rvPantry.layoutManager = LinearLayoutManager(activity)
        checkBoxRecycleviewAdapter = PantryTasksAdapter(getString(R.string.pantry)) { it->
            selectedItems= it as ArrayList<CheckBoxModel>
        }
        binding.rvPantry.adapter = checkBoxRecycleviewAdapter
        binding.filterLayout.llTimer.visibility=View.VISIBLE
        var currentHour=Utils.getCurrentHour()
        binding.filterLayout.selectTime.setText(currentHour)
        binding.filterLayout.ciCalender.setOnClickListener {
            setCalender()
        }
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
       /* binding.filterLayout.ctSelectAll.setOnClickListener(View.OnClickListener { v ->
            selectedItems.clear()
            listArr.forEach {
                var newAllSelectedItemsArr = ArrayList<CheckBoxModel>()
                (v as CheckedTextView).toggle()
//                var isChecked = false

                    listArr.forEach {
                        var chr = CheckBoxModel().apply {
                            position = 0
                            checked = v.isChecked
                            text = it.text
                        }
                        newAllSelectedItemsArr.add(chr)
                        if(v.isChecked){
                            selectedItems.add(CheckBoxModel(0, true, it.text))

                        }

                    }
                    listArr = newAllSelectedItemsArr
                    checkBoxRecycleviewAdapter.loadItems(listArr)

//                }


            }
        })*/
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
        binding.filterLayout.selectedDate.text = Utils.getCurrentDate()
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]
      /*  viewModel.getAllPantryTasksAssigned(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),
            isAssigned = true,
            isCompleted = false
        )

*/
//        viewModel.AllPantryTaksBydata("19-08-2024")
        refreshList()

        viewModel.getAllPantryPendingTasksByDate().observe(viewLifecycleOwner){
            listTaskData.clear()
            it.forEachIndexed { index, it ->
                var isCompleted="Pending"
                var date=it.created_date.split("-")
                if(it.isCompleted){
                    isCompleted="Completed"
                }
                listTaskData.add(TaskData(it.task_name, ""+index, "", date[0], date[1],it.AssignedTo ,isCompleted))
                listArr.add(CheckBoxModel(0, false, it.task_name,it.created_date,it.AssignedTo,it.id!!))
            }



            checkBoxRecycleviewAdapter.loadItems(listArr)

            binding.filterLayout.submit.setOnClickListener {
                if(selectedItems.size>0) {
                    Utils.confirmationAlertAlertDialog(
                        requireActivity(),
                        "Are you sure! Do you want to complete task ?"
                    ) { it1 ->
                        if (it1) {
                            updateData()
                        }
                    }
                }else{
                    Utils.showAlertDialog(requireActivity(),getString(R.string.please_select_at_least_one_item))

                }
            }
        }

    }
    fun updateData(){
        var selectedDate = binding.filterLayout.selectedDate.text.toString()
        var dataList= ArrayList<PantryTasks>()
        var lastIndex= selectedItems.size-1
        selectedItems.forEachIndexed { index, checkBoxModel ->
            val pantryTask= PantryTasks(checkBoxModel.text,binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),"", isAssigned = true, isCompleted = true,checkBoxModel.id)
            viewModel.updatePantryTasks(pantryTask)
            dataList.add(element = pantryTask)
            if(index==lastIndex){
                Thread.sleep(1000)
                println("lastIndex:: "+lastIndex+"  index:: "+index)
               selectedItems.clear()
                listArr.clear()
                checkBoxRecycleviewAdapter.loadItems(listArr)
                viewModel.getAllPantryTasksAssigned(binding.filterLayout.selectedDate.text.toString(),
                    binding.filterLayout.selectTime.text.toString(),
                    isAssigned = true,
                    isCompleted = false
                )
                Toast.makeText(context,"Tasks Completed Success..", Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun refreshList(){
        listArr.clear()
        checkBoxRecycleviewAdapter.loadItems(listArr)

        viewModel.getAllPantryTasksAssigned(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),true,false)
    }
 /* fun navigateToStatusUpdate(selectedItem: TaskData) {
        findNavController().navigate(
            R.id.fragment_status_update,
            bundleOf(AppConstants.BundleKey.HomeData to selectedItem)
        )
    }*/
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
                viewModel.getAllPantryTasksAssigned(dat,binding.filterLayout.selectTime.text.toString(), isAssigned = true, isCompleted = false)
                listArr.clear()
//                checkBoxRecycleviewAdapter.loadItems(listArr)


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
}