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
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.CommanAdapter
import com.dmss.dmssadminmaintanance.pantry.adapter.PantryTasksAdapter
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
        binding.filterLayout.llDate.visibility=View.VISIBLE
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
            setCalender()
        }
         checkBoxRecycleviewAdapter = PantryTasksAdapter { it ->
            println("FilteredList:: " + it.size)
            selectedItems = it as ArrayList<CheckBoxModel>

        }
        val layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.rvPantry.layoutManager = layoutManager

        binding.rvPantry.adapter = checkBoxRecycleviewAdapter
        binding.filterLayout.selectedDate.setText(Utils.getCurrentDate())

        viewModel.getAllRestRoomTasks(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),true)
        viewModel.getAllRestRoomTasksByDate().observe(viewLifecycleOwner){
            assigendList.clear()
            it.forEach {
                assigendList.add(it.task_name)
            }

           viewModel.getAllRestroomColumns()

        }
        viewModel.restRoomsAllCoulumns.observe(viewLifecycleOwner) { it ->
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
            coulumnNames.forEach {
                selectedItems.add(CheckBoxModel(0, true, it))

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
            /*   val formatter = SimpleDateFormat("yyyy-MM-dd")
               val date = Date()
               val current = formatter.format(date)*/




             openAssignedPersonDialog(assigendToPersonList)
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

        println("selectedDate:: "+selectedDate)
        val alertDialog: AlertDialog = shareAlertBuilder.create()
        layoutDailogListViewBinding.listView.adapter = CommanAdapter(requireActivity(), listArr)
        layoutDailogListViewBinding.listView.setOnItemClickListener { parent, view, position, id ->

            var selectedext = ""
            coulumnNames.forEach {it1->
                var isCompleted=false
                selectedItems.forEach { it2->
                    selectedext = selectedext + "," + it2.text
                    if(it2.text==it1){
                        isCompleted =true
                    }
                }
                pantryListDataArr.add( RestRoomTasks(it1,selectedDate.toString(),selectedTime.toString(),listArr[position],isCompleted,false) )
            }
            println("pantryListDataArr:: "+pantryListDataArr)
            val listlastSize=pantryListDataArr.lastIndex

            pantryListDataArr.forEachIndexed{ index, element ->
//                println("element:: "+element)
                viewModel.insertRestRoomTasks(element)
                if(index==listlastSize){
                    Thread.sleep(1000)
                    /* viewModel.getAllRestRoomTasks(binding.selectedDate.text.toString(),binding.selectTime.text.toString(),true)
                     listArr.clear()
                     checkBoxRecycleviewAdapter.loadItems(listArr)*/
                    refreshList()

                    Toast.makeText(context,"Tasks Assigned Success..", Toast.LENGTH_SHORT).show()

                }
            }
            /*  else if(dropFrom == getString(R.string.select_date)){
                  binding.etSelectDateInput.setText(listArr[position])
              }*/
            alertDialog.dismiss()
        }
        alertDialog.show()
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

        viewModel.getAllRestRoomTasks(binding.filterLayout.selectedDate.text.toString(),binding.filterLayout.selectTime.text.toString(),true)
    }
}