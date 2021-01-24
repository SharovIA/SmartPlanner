package com.ivanasharov.smartplanner.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    private lateinit var mNavigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavigationController = navHostFragment.navController
        setUpBottomNav(mNavigationController)

        val toolbar: Toolbar = findViewById(R.id.toolbar_actionbar)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.currentDayFragment, R.id.dailyScheduleFragment, R.id.planningFragment,
            R.id.weatherFragment))
        toolbar.setupWithNavController(mNavigationController, appBarConfiguration)
    //    initViewPager2withFragments()

    }

    private fun setUpBottomNav(navController: NavController){
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNav.setupWithNavController(navController)
    }

/*    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        NavigationUI.onNavDestinationSelected(item, mNavigationController)
        return super.onOptionsItemSelected(item)
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, mNavigationController)
                || super.onOptionsItemSelected(item)
    }
/*    private fun initViewPager2withFragments() {
        var adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter
        var names: ArrayList<String> = arrayListOf(getString(R.string.tab_1_current_day),
                                                      getString(R.string.tab_2_free_time),
                                                        getString(R.string.tab_3_planning),
                                                            getString(R.string.tab_4_info))
        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = names[position]
        }.attach()
    }*/

    override fun onDestroy() {
        super.onDestroy()
        Log.d("run", "MainActivity")
    }
}
