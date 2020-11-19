package com.ivanasharov.smartplanner.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.ivanasharov.smartplanner.BaseApplication
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.data.database.TaskDatabase
import com.ivanasharov.smartplanner.presentation.MyPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewPager2withFragments()

    }

    private fun initViewPager2withFragments() {
        var adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter
        var names: ArrayList<String> = arrayListOf(getString(R.string.tab_1_current_day),
                                                      getString(R.string.tab_2_free_time),
                                                        getString(R.string.tab_3_planning),
                                                            getString(R.string.tab_4_info))
        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = names[position]
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("run", "MainActivity")
    }
}
