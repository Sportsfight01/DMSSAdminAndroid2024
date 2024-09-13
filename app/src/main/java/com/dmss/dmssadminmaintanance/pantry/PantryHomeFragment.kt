package com.dmss.dmssadminmaintanance.pantry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.dmss.dmssadminmaintanance.BaseFragment
import com.dmss.dmssadminmaintanance.R
import com.dmss.dmssadminmaintanance.databinding.FragmentPantryHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PantryHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PantryHomeFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentPantryHomeBinding

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
        binding = FragmentPantryHomeBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    private fun initView(){




        val firstTab: TabLayout.Tab = binding.simpleTabLayout.newTab()
        firstTab.setText(getString(R.string.open)) // set the Text for the first Tab
        firstTab.setIcon(R.drawable.open_tab_icon) // set an icon for the

// first tab
        binding.simpleTabLayout.addTab(firstTab) // add  the tab at in the TabLayout

// Create a new Tab named "Second"
        val secondTab: TabLayout.Tab = binding.simpleTabLayout.newTab()
        secondTab.setText(getString(R.string.assigned)) // set the Text for the second Tab
        secondTab.setIcon(R.drawable.add_task) // set an icon for the second tab
        binding.simpleTabLayout.addTab(secondTab) // add  the tab  in the TabLayout

// Create a new Tab named "Third"
        val thirdTab: TabLayout.Tab = binding.simpleTabLayout.newTab()
        thirdTab.setText(getString(R.string.completed)) // set the Text for the first Tab
        thirdTab.setIcon(R.drawable.update_task) // set an icon for the first tab
        binding.simpleTabLayout.addTab(thirdTab)

        navigateTo(PantryTasksListFragment())

        // perform setOnTabSelectedListener event on TabLayout
        binding.simpleTabLayout.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
// get the current selected tab's position and replace the fragment accordingly
                var fragment: Fragment? = null
                when (tab.position) {
                    0 -> fragment = PantryTasksListFragment()
                    1 -> fragment = PendingTaskListFragment()
                    2 -> fragment = CompletedTaskListFragment()
                }
                navigateTo(fragment!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }
    fun navigateTo(fragment: Fragment){
        val fm: FragmentManager = requireActivity().getSupportFragmentManager()
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.simpleFrameLayout, fragment!!)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }
}