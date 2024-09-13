package com.dmss.dmssadminmaintanance.pantry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.databinding.FragmentPendingTaskListBinding
import com.dmss.dmssadminmaintanance.model.TaskData
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.TasksListStatusAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * A simple [Fragment] subclass.
 * Use the [CompletedTaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompletedTaskListFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentPendingTaskListBinding
    private lateinit var viewModel: MaintainanceViewModel

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
        binding.filterLayout.selectedDate.setText(Utils.getCurrentDate())
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]
        viewModel.getAllPantryTasksCompeted(binding.filterLayout.selectedDate.text.toString(), isAssigned = true, isCompleted = true)

        var listTaskData = ArrayList<TaskData>()
        binding.filterLayout.ciCalender.setOnClickListener {
            setCalender()
        }

//        viewModel.AllPantryTaksBydata("19-08-2024")
        viewModel.getAllPantryCompletedTasksByDate().observe(viewLifecycleOwner){
            println("getAllPantryTasksbydate Fragment :: "+it)
            listTaskData.clear()
            it.forEachIndexed { index, it ->
                var isCompleted="Pending"
                var date=it.created_date.split("-")
                if(it.isCompleted){
                    isCompleted="Completed"
                }
                    listTaskData.add(TaskData(it.task_name, ""+index, "", date[0], date[1], isCompleted))
            }


            binding.rvPantry.layoutManager = LinearLayoutManager(activity)
            var checkBoxRecycleviewAdapter = TasksListStatusAdapter(listTaskData) {
                println("FilteredList:: " + it)
//                navigateToStatusUpdate(it)

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
    private fun setCalender(){
        val c = Calendar.getInstance()

        // on below line we are getting
        // our day, month and year.
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // on below line we are creating a
        // variable for date picker dialog.
        val datePickerDialog = android.app.DatePickerDialog(
            // on below line we are passing context.
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                binding.filterLayout.selectedDate.setText(dat)
                viewModel.getAllPantryTasksCompeted(dat, isAssigned = true, isCompleted = true)

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