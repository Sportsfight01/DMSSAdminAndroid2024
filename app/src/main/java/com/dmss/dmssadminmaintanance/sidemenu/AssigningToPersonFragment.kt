package com.dmss.dmssadminmaintanance.sidemenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.databinding.FragmentAssigningToPersonBinding
import com.dmss.dmssadminmaintanance.databinding.FragmentDownloadExcelsBinding
import com.dmss.dmssadminmaintanance.db.AssignTaskToPersonEntityData
import com.dmss.dmssadminmaintanance.model.Utils

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
        initView()
//        requestViewModel()
//        Observers()
        return binding.root
    }
    private fun initView(){


        binding.assign.setOnClickListener {
             mName = binding.etNameInput.text.toString()
            if(mName ==""){
                Toast.makeText(requireActivity(),"Please enter name",Toast.LENGTH_SHORT).show()
            }else{
                viewModel.insertTaskAssigningPersons(AssignTaskToPersonEntityData(mName,Utils.getCurrentDate()))
                Utils.showAlertDialog(requireActivity(),"Person Added Success")
            }
        }

    }
    private fun requestViewModel(){
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]

    }
}