package com.dmss.dmssadminmaintanance.dashboard

import android.annotation.SuppressLint
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
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.databinding.FragmentDashboardBinding
import com.dmss.dmssadminmaintanance.databinding.LayoutDailogListViewBinding
import com.dmss.dmssadminmaintanance.model.Utils
import com.dmss.dmssadminmaintanance.pantry.adapter.CommanAdapter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : BaseFragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentDashboardBinding
    private  var genderList :ArrayList<String> = ArrayList<String>()

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
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }
    fun navigateTo(resId: Int){
        val bundle = Bundle()
//        bundle.putParcelable(AppConstants.BundleKey.HomeData, selectedItem)
        findNavController().navigate(
            resId,
            bundle
        )
    }
    fun initView(){


        binding.cvCabins.setOnClickListener {
//            navigateTo(R.id.fragment_tasks_list)
        }
        binding.cvGamesRoom.setOnClickListener {
//            navigateTo(R.id.create_report_fragment)

//            createXcelFile()

        }
        binding.cvWorkStation.setOnClickListener {
            navigateTo(R.id.task_list_fragment)
        }
        binding.cvPantry.setOnClickListener {
            navigateTo(R.id.pantryHomeFragment)
        }
        binding.cvRestRooms.setOnClickListener {
            genderList = resources.getStringArray(R.array.gender) .toList() as ArrayList<String>
//            genderList.add("Male")
//            genderList.add("Female")
            openDialog("Select Gender",genderList)
        }
        binding.cvGenralArea.setOnClickListener {
//            Toast.makeText(requireActivity(),"Development in progress..", Toast.LENGTH_SHORT).show()
            navigateTo(R.id.download_xls_fragment)

        }
    }

    private fun createCodeFile(): File
    {
        var currentCodePath:String=""
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = ContextCompat.getDataDir(requireActivity())!!

        println("storageDir:: "+storageDir)
        return File.createTempFile("Durga${timeStamp}_", ".xls", storageDir).apply {
            currentCodePath = absolutePath
        }
    }
    private fun openDialog(dropFrom: String, listArr: ArrayList<String>) {

        var layoutDailogListViewBinding = LayoutDailogListViewBinding.inflate(layoutInflater)
        val shareAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        shareAlertBuilder.setView(layoutDailogListViewBinding.root)
        layoutDailogListViewBinding.tvHeader.text = dropFrom

        val alertDialog: AlertDialog = shareAlertBuilder.create()
        layoutDailogListViewBinding.listView.adapter = CommanAdapter(requireActivity(), listArr)
        layoutDailogListViewBinding.listView.setOnItemClickListener { parent, view, position, id ->

           Utils.selectedGender=listArr[position]
           navigateTo(R.id.restRoomHomeFragment)
            /*  else if(dropFrom == getString(R.string.select_date)){
                  binding.etSelectDateInput.setText(listArr[position])
              }*/
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}