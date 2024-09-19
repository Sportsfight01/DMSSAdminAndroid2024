package com.dmss.dmssadminmaintanance

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dmss.dmssadminmaintanance.dashboard.DashboardFragment
import com.dmss.dmssadminmaintanance.pantry.PantryHomeFragment
import com.dmss.dmssadminmaintanance.pantry.RestRoomHomeFragment
import com.dmss.dmssadminmaintanance.sidemenu.AssigningToPersonFragment

open class BaseFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        println("parentFragmentManager:: "+parentFragmentManager.primaryNavigationFragment)
        when (parentFragmentManager.primaryNavigationFragment) {

            is DashboardFragment -> {
//                changeStatusBarColor(R.color.white)
                (activity as MainActivity).initToolBar(false)
                (activity as MainActivity).initToolBarHeader(getString(R.string.dmss))

            }
            is PantryHomeFragment ->{
                (activity as MainActivity).initToolBarHeader(getString(R.string.pantry))

            }
            is RestRoomHomeFragment ->{
                (activity as MainActivity).initToolBarHeader(getString(R.string.rest_rooms))

            }
            is AssigningToPersonFragment ->{
                (activity as MainActivity).initToolBarHeader(getString(R.string.assign))

            }
            else ->{
                (activity as MainActivity).initToolBar(true)

            }
            /* is ProgressDetailsFragment ->{
                changeStatusBarColor(R.color.new_gray_color)
                (activity as DashboardNewActivity).initToolBarWithBackBackButton(isTrasToolBar = true)
            }
            else ->{
                (activity as DashboardNewActivity).updateToolBarTitle()
                changeStatusBarColor(R.color.new_gray_color)
            }*/
        }
    }

    open fun changeStatusBarColor() {
        changeStatusBarColor(Color.WHITE)
    }

    open fun changeStatusBarColor(color: Int) {
        val window: Window = requireActivity().window
        val statusBarColor = ContextCompat.getColor(requireContext(), color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (color == R.color.white) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else window.decorView.systemUiVisibility = 0
            window.statusBarColor = statusBarColor
        }
    }
}
