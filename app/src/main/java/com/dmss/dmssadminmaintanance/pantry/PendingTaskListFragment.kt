package com.dmss.dmssadminmaintanance.pantry

import android.app.DatePickerDialog
import android.os.Bundle
import android.provider.ContactsContract.Contacts
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.BaseFragment
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
        binding.filterLayout.ciCalender.setOnClickListener {
            setCalender()
        }

        binding.filterLayout.selectedDate.text = Utils.getCurrentDate()
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]
        viewModel.getAllPantryTasksAssigned(binding.filterLayout.selectedDate.text.toString(),
            isAssigned = true,
            isCompleted = false
        )

//        viewModel.AllPantryTaksBydata("19-08-2024")
        viewModel.getAllPantryPendingTasksByDate().observe(viewLifecycleOwner){
            listTaskData.clear()
            it.forEachIndexed { index, it ->
                var isCompleted="Pending"
                var date=it.created_date.split("-")
                if(it.isCompleted){
                    isCompleted="Completed"
                }
                listTaskData.add(TaskData(it.task_name, ""+index, "", date[0], date[1], isCompleted))
                listArr.add(CheckBoxModel(0, false, it.task_name,it.created_date,it.id!!))
            }


            binding.rvPantry.layoutManager = LinearLayoutManager(activity)
            var checkBoxRecycleviewAdapter = PantryTasksAdapter {it->
                selectedItems= it as ArrayList<CheckBoxModel>
            }
            binding.rvPantry.adapter = checkBoxRecycleviewAdapter
            checkBoxRecycleviewAdapter.loadItems(listArr)

            binding.filterLayout.submit.setOnClickListener {
                var selectedDate = binding.filterLayout.selectedDate.text.toString()
                var dataList= ArrayList<PantryTasks>()
                var lastIndex= selectedItems.size-1
                selectedItems.forEachIndexed { index, checkBoxModel ->
                    val pantryTask= PantryTasks(checkBoxModel.text,selectedDate, isAssigned = true, isCompleted = true,checkBoxModel.id)
                    viewModel.updatePantryTasks(pantryTask)
                    dataList.add(element = pantryTask)
                    if(index==lastIndex){
                        Thread.sleep(1000)
                        println("lastIndex:: "+lastIndex+"  index:: "+index)

                        listArr.clear()
                        checkBoxRecycleviewAdapter.loadItems(listArr)
                        viewModel.getAllPantryTasksAssigned(binding.filterLayout.selectedDate.text.toString(),
                            isAssigned = true,
                            isCompleted = false
                        )
                        Toast.makeText(context,"Tasks Completed Success..", Toast.LENGTH_SHORT).show()

                    }
                }
            }


        }


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
                viewModel.getAllPantryTasksAssigned(dat, isAssigned = true, isCompleted = false)

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