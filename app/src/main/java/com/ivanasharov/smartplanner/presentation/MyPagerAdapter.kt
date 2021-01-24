package com.ivanasharov.smartplanner.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ivanasharov.smartplanner.presentation.view.CurrentDayFragment
import com.ivanasharov.smartplanner.presentation.view.DailyScheduleFragment
import com.ivanasharov.smartplanner.presentation.view.WeatherFragment
import com.ivanasharov.smartplanner.presentation.view.PlanningFragment

 class MyPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fm, lifecycle){

        var fragments: ArrayList<Fragment> = arrayListOf(
            CurrentDayFragment(),
            DailyScheduleFragment(),
            PlanningFragment(),
            WeatherFragment()
        )
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment {
          return fragments[position]
        }
}