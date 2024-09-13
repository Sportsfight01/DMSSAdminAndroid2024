package com.dmss.dmssadminmaintanance.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
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
            navigateTo(R.id.restRoomHomeFragment)
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
  /*  private fun createXcelFile(){
        *//*val filePath = Environment.getExternalFilesDir()
            .absolutePath + "/MyFolder/"*//*
        val filePath = createCodeFile().path
        println("filePath::"+filePath)
        try {

            val root = File(filePath)
//            if (!root.exists()) {
            root.mkdirs()
//            }
            val f = File(filePath)
            if (f.exists()) {
                f.delete()
            }
            f.createNewFile()

            val out = FileOutputStream(f)
            println("FileOutputStream::"+out)
            out.flush()
            out.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        val root: File = File(filePath)
        println("root:: "+root)
        *//*  var  filePath = File(
              rootPath )
           println("filePath:: "+filePath)
   *//*
        try {
            if (!root.exists()) {
                val hssfWorkbook = HSSFWorkbook()
                val hssfSheet = hssfWorkbook.createSheet("MySheet")

                val hssfRow = hssfSheet.createRow(0)
                val hssfCell = hssfRow.createCell(0)

                hssfCell.setCellValue("Name")
                root.createNewFile()
                val fileOutputStream: FileOutputStream = FileOutputStream(filePath)
                hssfWorkbook.write(fileOutputStream)
//                eName.setText("")
                Toast.makeText(activity, "File Created", Toast.LENGTH_SHORT).show()

                if (fileOutputStream != null) {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                }
            } else {
                val fileInputStream: FileInputStream = FileInputStream(filePath)
                val hssfWorkbook = HSSFWorkbook(fileInputStream)
                val hssfSheet = hssfWorkbook.getSheetAt(0)
                var lastRowNum = hssfSheet.lastRowNum

                val hssfRow = hssfSheet.createRow(++lastRowNum)
                hssfRow.createCell(0).setCellValue("Durga Prasad")

                fileInputStream.close()

                val fileOutputStream: FileOutputStream = FileOutputStream(filePath)
                hssfWorkbook.write(fileOutputStream)
//                eName.setText("")
                Toast.makeText(activity, "File Updated", Toast.LENGTH_SHORT).show()
                fileOutputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/
}