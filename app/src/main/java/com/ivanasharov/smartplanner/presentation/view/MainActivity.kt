package com.ivanasharov.smartplanner.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.MyPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter

        tabs.setupWithViewPager(viewPager)
    }
}
