package com.example.epamandroid.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var isVisibleItem: Boolean = true

    companion object {
        const val TITLE_KEY: String = "titleKey"
        const val MENU_VISIBILITY_KEY: String = "menuVisibilityKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(mainActivityCustomActionBarLayout as Toolbar?)

        when {
            (savedInstanceState == null) -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainActivityFrameLayout, HomeFragment())
                        .commit()
                setTitle(R.string.home_page)
            }
            else -> {
                title = savedInstanceState.getString(TITLE_KEY)
                isVisibleItem = savedInstanceState.getBoolean(MENU_VISIBILITY_KEY)
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
                setTitle(R.string.home_page)
                setMenuVisibility(true)
            }
            R.id.bottomNavigationSettings -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainActivityFrameLayout, SettingsFragment())
                        .commit()
                setTitle(R.string.settings)
                setMenuVisibility(false)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_action_bar_menu, menu)

        setMenuVisibility(isVisibleItem)

        return super.onCreateOptionsMenu(menu)
    }

    private fun setMenuVisibility(isVisible: Boolean) {
        (mainActivityCustomActionBarLayout as Toolbar?)
                ?.menu
                ?.getItem(0)
                ?.isVisible = isVisible
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString(TITLE_KEY, title.toString())
        (mainActivityCustomActionBarLayout as Toolbar?)
                ?.menu
                ?.getItem(0)
                ?.isVisible
                ?.let {
                    outState
                            ?.putBoolean(MENU_VISIBILITY_KEY, it)
                }
    }
}
