package com.dmss.dmssadminmaintanance

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.emptyLongSet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.dmss.admin.db.viewmodel.MaintainanceViewModel
import com.dmss.dmssadminmaintanance.databinding.LayoutMainBinding
import com.dmss.dmssadminmaintanance.db.MaintenanceDB
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    lateinit var viewModel:MaintainanceViewModel
    lateinit var binding: LayoutMainBinding
    private lateinit var database: MaintenanceDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDrawerMenuClickListener()
        setupNavController()
        initView()
        setViewModel()
        /*  enableEdgeToEdge()
        setContent {
            DMSSAdminMaintananceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    getImage(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }*/
    }
    private fun navigateTo(resId: Int) {
        if (checkCurrentFragment(resId).not())
            navController.navigate(resId)
    }
    private fun checkCurrentFragment(id : Int): Boolean{
        val navController =
            Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard_new)
        return navController.currentDestination?.id == id
    }

    private fun initView() {


        checkPermission(applicationContext, this)
        binding.contentMain.ivBack.setOnClickListener {
            onNavigateBackPressed()

        }
        binding.contentMain.ivNavMenu.setOnClickListener {
            toggleLeftDrawer()

        }
        binding.leftDrawerMenu.ivNavClose.setOnClickListener {
            toggleLeftDrawer()


        }

        binding.leftDrawerMenu.clDownload.setOnClickListener {
            toggleLeftDrawer()
             navigateTo(R.id.download_xls_fragment)
        }
        binding.leftDrawerMenu.clAssign.setOnClickListener {
            toggleLeftDrawer()
            navigateTo(R.id.assign_person_fragment)
        }

//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.fragment_dashboard,
//            R.id.pantryHomeFragment))
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navController.navigate(R.id.fragment_dashboard)
//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.fragment_dashboard,
//            R.id.pantryHomeFragment))
//        setupActionBarWithNavController(navController, appBarConfiguration)


    }
    private fun toggleLeftDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    private fun setDrawerMenuClickListener() {


        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
            }
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}

        })
    }

    fun setViewModel(){
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val current = formatter.format(date)

//        viewModel = ViewModelProvider.of(this)[MaintainanceViewModel::class.java]
        viewModel = ViewModelProvider(this)[MaintainanceViewModel::class.java]


        database = MaintenanceDB.getDatabase(this)

    }
    private fun setupNavController() {
        navController = findNavController(R.id.nav_host_fragment_content_dashboard_new)
    }
    fun checkPermission(t: Context?, s: Activity?) {
        //check condition

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )

        }
    }
    fun initToolBar(visible:Boolean){
        if(visible) {
            binding.contentMain.ivBack.visibility = View.VISIBLE
            binding.contentMain.ivNavMenu.visibility = View.GONE
            binding.contentMain.btSubmit.visibility = View.GONE

        }else{
            binding.contentMain.ivBack.visibility = View.GONE
            binding.contentMain.ivNavMenu.visibility = View.VISIBLE
            binding.contentMain.btSubmit.visibility = View.GONE

        }
    }
    fun initToolBarHeader(text:String){
        binding.contentMain.tvToolbarSubHeaderTitle.text = text
    }


    private fun onNavigateBackPressed() {
        val navController =
            Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard_new)
        println("currentDestination:: "+navController.currentDestination?.id)
        println("currentDestination task_list_fragment:: "+ R.id.fragment_dashboard)

        when (navController.currentDestination?.id) {
            R.id.fragment_dashboard -> {
                finish()
//                finishAffinity()
            }
            else -> {
                super.onBackPressed()
//                navigateTo(R.id.dashboard_fragment)
//                supportFragmentManager.popBackStackImmediate();


            }
        }
    }
    override fun onBackPressed() {
        onNavigateBackPressed()
//                super.onBackPressed()

    }
}

@Composable
fun getImage(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


