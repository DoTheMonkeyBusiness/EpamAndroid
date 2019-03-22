package com.example.epamandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val NAVIGATION_HEADER_ICON_COLOR_KEY: String = "navigationHeaderIconButton"
    }

    private lateinit var navigationHeaderView: NavigationHeaderView

    private var currentIconColor: String? = null

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

        navigationHeaderView =
                activityMainNavView
                        .getHeaderView(0)
                        .findViewById(R.id.headerView)

        activityMainNavView.setNavigationItemSelectedListener(this)

        when {
            (savedInstanceState == null) -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activityMainFragmentContainer, FirstFragment())
                        .commit()

                activityMainNavView.setCheckedItem(R.id.navFragmentFirst)
            }
            (savedInstanceState.getString(NAVIGATION_HEADER_ICON_COLOR_KEY) != null) -> {
                navigationHeaderView.updateIconColor(savedInstanceState.getString(NAVIGATION_HEADER_ICON_COLOR_KEY))
            }
        }
        navigationHeaderView.setOnClickListener {
            currentIconColor = colorList.randomPosition()
            navigationHeaderView.updateIconColor(currentIconColor)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navFragmentFirst -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activityMainFragmentContainer, FirstFragment())
                        .commit()
            }
            R.id.navFragmentSecond -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activityMainFragmentContainer, SecondFragment())
                        .commit()
            }
        }

        activityMainDrawerLayout.closeDrawer(GravityCompat.START)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                activityMainDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configureSupportActionBar() {
        setSupportActionBar(activityMainToolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white)
        }
    }

    private fun <T> List<T>.randomPosition(): T {
        return this[Random.nextInt(this.size)]
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString(NAVIGATION_HEADER_ICON_COLOR_KEY, currentIconColor)
    }
}
