package com.ivanasharov.smartplanner.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ivanasharov.smartplanner.presentation.view.CurrentDayFragment
import com.ivanasharov.smartplanner.presentation.view.FreeTimeFragment
import com.ivanasharov.smartplanner.presentation.view.InformationFragment
import com.ivanasharov.smartplanner.presentation.view.PlanningFragment

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> CurrentDayFragment()
            1 -> FreeTimeFragment()
            2 -> PlanningFragment()
            else -> {
                return InformationFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Current day"
            1 -> "Free time"
            2 -> "Planning"
            else -> {
                return "Info"
            }
        }
    }

}