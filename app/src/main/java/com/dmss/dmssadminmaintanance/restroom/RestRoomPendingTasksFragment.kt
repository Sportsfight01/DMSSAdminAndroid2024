package com.dmss.dmssadminmaintanance.restroom

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.databinding.FragmentPendingTaskListBinding
import com.dmss.dmssadminmaintanance.db.PantryTasks
import com.dmss.dmssadminmaintanance.db.RestRoomTasks
import com.dmss.dmssadminmaintanance.model.CheckBoxModel
import com.dmss.dmssadminmaintanance.model.TaskData
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.PantryTasksAdapter
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RestRoomPendingTasksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestRoomPendingTasksFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentPendingTaskListBinding
    private lateinit var viewModel: MaintainanceViewModel
    var listArr: MutableList<CheckBoxModel> = ArrayList<CheckBoxModel>()

    var selectedItems: MutableList<CheckBoxModel> = ArrayList<CheckBoxModel>()
    lateinit var checkBoxRecycleviewAdapter:PantryTasksAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingTaskListBinding.inflate(inflater, container, false)
        requestViewModel()
        initView()

        return binding.root
    }
    private fun initView() {

        var listTaskData = ArrayList<TaskData>()
//        binding.llDate.visibility=View.VISIBLE
        binding.filterLayout.ciTimer.visibility=View.VISIBLE
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
        binding.filterLayout.ctSelectAll.setOnClickListener(View.OnClickListener { v ->
            (v as CheckedTextView).toggle()
            listArr.forEach {
                selectedItems.add(CheckBoxModel(0, true, it.text))
//                listArr1.add(CheckBoxModel(0, true, it.text))
            }
            listArr=selectedItems
//            listArr.clear()
            println("listArr" + listArr.size)
            checkBoxRecycleviewAdapter.loadItems(listArr)

        })
        binding.filterLayout.selectedDate.text = Utils.getCurrentDate()
        viewModel.getAllRestRoomTasksAssigned(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),
            isAssigned = true,
            isCompleted = false
        )

//        viewModel.AllPantryTaksBydata("19-08-2024")
        viewModel.getAllRestRoomPendingTasksDataBydate().observe(viewLifecycleOwner){
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
             checkBoxRecycleviewAdapter = PantryTasksAdapter {it->
                selectedItems= it as ArrayList<CheckBoxModel>
            }

            binding.rvPantry.adapter = checkBoxRecycleviewAdapter
            checkBoxRecycleviewAdapter.loadItems(listArr)

            binding.filterLayout.submit.setOnClickListener {
                var lastIndex= selectedItems.lastIndex
                selectedItems.forEachIndexed { index, checkBoxModel ->
                    val restRoomTasks= RestRoomTasks(checkBoxModel.text,binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),"", isAssigned = true, isCompleted = true,checkBoxModel.id)
                    viewModel.updateRestRoomTasks(restRoomTasks)
                    if(index==lastIndex){
                        Thread.sleep(1000)
                        refreshList()
                        Toast.makeText(context,"Tasks Completed Success..", Toast.LENGTH_SHORT).show()

                    }
                }
            }


        }


    }
    fun requestViewModel(){
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]

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
                refreshList()

            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
    private fun refreshList(){
        viewModel.getAllRestRoomTasksAssigned(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(), isAssigned = true, isCompleted = false)
        listArr.clear()
        checkBoxRecycleviewAdapter.loadItems(listArr)
    }
}