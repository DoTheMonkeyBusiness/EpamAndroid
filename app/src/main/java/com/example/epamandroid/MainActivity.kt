package com.example.epamandroid

import android.graphics.Color.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.activity_main_toolbar
import kotlinx.android.synthetic.main.activity_main.activity_main_drawer_layout
import kotlinx.android.synthetic.main.activity_main.activity_main_nav_view

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val NAVIGATION_HEADER_ICON_BUTTOUN_KEY: String = "navigationHeaderIconButton"
    }

    private var navigationHeaderIconButtonColor: String? = null

    private val colorList = listOf(
        "#FFAB00",
        "#E64A19",
        "#00E676",
        "#00B8D4",
        "#01579B",
        "#651FFF",
        "#26C6DA",
        "#F44336",
        "#EF6C00"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureSupportActionBar()

        val navigationHeaderIconButton = activity_main_nav_view
            .inflateHeaderView(R.layout.navigation_header)
            .findViewById<ImageView>(R.id.navigation_header_android_icon)
        activity_main_nav_view.setNavigationItemSelectedListener(this)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.activity_main_fragment_container, FirstFragment())
                .commit()
            activity_main_nav_view.setCheckedItem(R.id.nav_fragment_first)
        } else {
            navigationHeaderIconButton.setColorFilter(
                parseColor(
                    savedInstanceState.getString(
                        NAVIGATION_HEADER_ICON_BUTTOUN_KEY
                    )
                )
            )
        }

        navigationHeaderIconButton.setOnClickListener {
            navigationHeaderIconButton.setColorFilter(calculateRandomListPosition())
//            navigationHeaderIconButtonColor = navigationHeaderIconButton
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_fragment_first -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_fragment_container, FirstFragment()).commit()
            }
            R.id.nav_fragment_second -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_fragment_container, SecondFragment()).commit()
            }
        }

        activity_main_drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                activity_main_drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configureSupportActionBar() {
        setSupportActionBar(activity_main_toolbar)

        supportActionBar.apply {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setHomeAsUpIndicator(R.drawable.ic_menu_white)
        }
    }

    private fun calculateRandomListPosition(): Int {
        return parseColor(colorList[(Math.random() * 100 % (colorList.size - 1)).toInt()])
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

//        outState?.putString(NAVIGATION_HEADER_ICON_BUTTOUN_KEY, navigationHeaderIconButtonColor)
    }
}
