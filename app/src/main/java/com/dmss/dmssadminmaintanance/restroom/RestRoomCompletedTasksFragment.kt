package com.dmss.dmssadminmaintanance.restroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.databinding.FragmentPendingTaskListBinding
import com.dmss.dmssadminmaintanance.model.TaskData
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.TasksListStatusAdapter
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RestRoomCompletedTasksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestRoomCompletedTasksFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentPendingTaskListBinding
    private lateinit var viewModel: MaintainanceViewModel
    private lateinit var  checkBoxRecycleviewAdapter:TasksListStatusAdapter
    var listTaskData = ArrayList<TaskData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        binding.filterLayout.llTimer.visibility=View.VISIBLE
        binding.filterLayout.submit.visibility=View.GONE
        binding.filterLayout.tvAll.visibility=View.GONE
        binding.filterLayout.ctSelectAll.visibility=View.GONE

        var currentHour=Utils.getCurrentHour()
        binding.filterLayout.selectTime.setText(currentHour)
        binding.filterLayout.ciTimer.visibility=View.VISIBLE
        binding.filterLayout.selectedDate.setText(Utils.getCurrentDate())
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]
        refreshList()
//        viewModel.getAllRestRoomTasksCompleted(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(), isAssigned = true, isCompleted = true)
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

//        viewModel.AllPantryTaksBydata("19-08-2024")
        viewModel.getAllRestRoomCompletedTasksDataBydate().observe(viewLifecycleOwner){
            println("getAllPantryTasksbydate Fragment :: "+it)
            listTaskData.clear()
            it.forEachIndexed { index, it ->
                var isCompleted="Pending"
                var date=it.created_date.split("-")
                if(it.isCompleted){
                    isCompleted="Completed"
                }
                listTaskData.add(TaskData(it.task_name, ""+index, "", date[0], date[1], it.AssignedTo,isCompleted))
            }


            binding.rvPantry.layoutManager = LinearLayoutManager(activity)
             checkBoxRecycleviewAdapter = TasksListStatusAdapter(listTaskData) {
                println("FilteredList:: " + it)

            }
            binding.rvPantry.adapter = checkBoxRecycleviewAdapter

        }
        viewModel.getFemaleRestroomCompletedTasksByDateTime().observe(viewLifecycleOwner){
            println("getAllPantryTasksbydate Fragment :: "+it)
            listTaskData.clear()
            it.forEachIndexed { index, it ->
                var isCompleted="Pending"
                var date=it.created_date.split("-")
                if(it.isCompleted){
                    isCompleted="Completed"
                }
                listTaskData.add(TaskData(it.task_name, ""+index, "", date[0], date[1], it.AssignedTo,isCompleted))
            }


            binding.rvPantry.layoutManager = LinearLayoutManager(activity)
            checkBoxRecycleviewAdapter = TasksListStatusAdapter(listTaskData) {
                println("FilteredList:: " + it)

            }
            binding.rvPantry.adapter = checkBoxRecycleviewAdapter

        }


    }
    /* fun navigateToStatusUpdate(selectedItem: TaskData) {
         findNavController().navigate(
             R.id.fragment_status_update,
             bundleOf(AppConstants.BundleKey.HomeData to selectedItem)
         )
     }*/

    private fun refreshList(){
        if(Utils.selectedGender==getString(R.string.male)) {
            viewModel.getAllRestRoomTasksCompleted(
                binding.filterLayout.selectedDate.text.toString(),
                binding.filterLayout.selectTime.text.toString(),
                isAssigned = true,
                isCompleted = true
            )
        }else{
            viewModel.sendRequestToFemaleRestRoomCompletedTasks(
                binding.filterLayout.selectedDate.text.toString(),
                binding.filterLayout.selectTime.text.toString(),
                isAssigned = true,
                isCompleted = true
            )
        }
        listTaskData.clear()
    }
}