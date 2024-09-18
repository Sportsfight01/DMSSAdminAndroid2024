package com.dmss.dmssadminmaintanance.sidemenu

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.databinding.FragmentAssigningToPersonBinding
import com.dmss.dmssadminmaintanance.databinding.FragmentDownloadExcelsBinding
import com.dmss.dmssadminmaintanance.db.AssignTaskToPersonEntityData
import com.dmss.dmssadminmaintanance.model.CheckBoxModel
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.AssignPersonAdapter
import com.dmss.dmssadminmaintanance.pantry.adapter.PantryTasksAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AssigningToPersonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AssigningToPersonFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentAssigningToPersonBinding
    var tasksList = ArrayList<String>()
    var listOfRestRoomColumns = ArrayList<String>()
    var listOfRestRoomTimingsColumns = ArrayList<String>()
    var mName =""
    private lateinit var viewModel: MaintainanceViewModel
    lateinit var assignPersonAdapter:AssignPersonAdapter
    var assigendToPersonList = ArrayList<AssignTaskToPersonEntityData>()

    var selectedDate=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAssigningToPersonBinding.inflate(inflater, container, false)
        requestViewModel()
        observer()

        initView()

//        requestViewModel()
//        Observers()
        return binding.root
    }
    private fun initView(){

        val layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.rcAssign.layoutManager = layoutManager
        assignPersonAdapter = AssignPersonAdapter() { it ->
//            var selectedItems = it as ArrayList<String>
            println("AssignPersonAdapter::"+it)
            deleteAlert(requireActivity(),"Do you want to delete item",it)

        }
        binding.rcAssign.adapter=assignPersonAdapter
        binding.assign.setOnClickListener {
             mName = binding.etNameInput.text.toString()
            if(mName ==""){
                Toast.makeText(requireActivity(),"Please enter name",Toast.LENGTH_SHORT).show()
            }else{
                viewModel.insertTaskAssigningPersons(AssignTaskToPersonEntityData(mName,Utils.getCurrentDate()))
                binding.etNameInput.setText("")
                Utils.showAlertDialog(requireActivity(),"Person Added Success")
                refreshList()

            }
        }

    }
    private fun requestViewModel(){
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]
        refreshList()

    }
    private fun refreshList() {
        viewModel.getAssignedPersonRequest()

    }
        fun observer(){
            viewModel.getAssignTaskPersons().observe(viewLifecycleOwner){
//                assigendToPersonList.clear()
                /*it.forEach {
                    assigendToPersonList.add(it.name)
                }*/
                println("assigendToPersonList::"+assigendToPersonList)
                assigendToPersonList = it as ArrayList<AssignTaskToPersonEntityData>
                assignPersonAdapter.loadItems(assigendToPersonList)

            }
    }
    fun deleteAlert(context: Context, title:String,assignTaskToPersonEntityData: AssignTaskToPersonEntityData)
    {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(title)
        builder.setTitle(context.getString(R.string.dmss))
        builder.setCancelable(false)
        builder.setPositiveButton(
            Html.fromHtml("<font color=" + context.resources.getColor(R.color.black) + ">OK</font>"),
            DialogInterface.OnClickListener {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()
                dialog.dismiss()

             viewModel.deletedAssignTaskToPerson(assignTaskToPersonEntityData)
                Thread.sleep(300)
                refreshList()
            })
         builder.setNegativeButton(
             Html.fromHtml("<font color=" + this.resources.getColor(R.color.black) + ">No</font>"),
             DialogInterface.OnClickListener {
                     dialog: DialogInterface, _: Int -> dialog.dismiss()
                 dialog.dismiss()
             })
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }
}