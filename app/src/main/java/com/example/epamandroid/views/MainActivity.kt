package com.example.epamandroid.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when {
            (savedInstanceState == null) -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainActivityFrameLayout, HomeFragment())
                        .commit()
            }
        }

        (mainActivityBottomNavigationView as BottomNavigationView).setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottomNavigationHome -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainActivityFrameLayout, HomeFragment())
                        .commit()
            }
            R.id.bottomNavigationSettings -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainActivityFrameLayout, SettingsFragment())
                        .commit()
            }
        }
        return true
    }
}
