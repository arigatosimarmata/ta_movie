package com.example.tamovie.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tamovie.R
import com.example.tamovie.adapter.TabAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.tab_layout
import kotlinx.android.synthetic.main.activity_home.view_pager

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tabAdapter = TabAdapter(supportFragmentManager, lifecycle)
        view_pager.adapter = tabAdapter

        val tabTitles = arrayOf("Popular", "Now Playing")
        TabLayoutMediator(tab_layout, view_pager){ tab, position ->
            tab.text = tabTitles[position]

        }.attach()

    }
}