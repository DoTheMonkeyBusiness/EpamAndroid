package com.example.epamandroid.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.epamandroid.R
import com.example.epamandroid.contracts.MainContract
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, MainContract.View {

    private var isVisibleItem: Boolean = true

    companion object {
        const val TITLE_KEY: String = "titleKey"
        const val MENU_VISIBILITY_KEY: String = "menuVisibilityKey"
        const val HOME_FRAGMENT_TAG_KEY: String = "homeFragmentTagKey"
        const val SETTINGS_FRAGMENT_TAG_KEY: String = "settingsFragmentTagKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val isDarkModeEnabled = PreferenceManager.getDefaultSharedPreferences(this)

        if (isDarkModeEnabled.getBoolean(getString(R.string.switch_day_night_mode_key), false)) {
            setTheme(R.style.AppThemeNight)
        } else {
            setTheme(R.style.AppThemeDay)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(mainActivityCustomActionBarLayout as Toolbar?)

        when {
            (savedInstanceState == null) -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainActivityFrameLayout, HomeFragment(), HOME_FRAGMENT_TAG_KEY)
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
        val homeFragment = supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG_KEY)
        val settingsFragment = supportFragmentManager.findFragmentByTag(SETTINGS_FRAGMENT_TAG_KEY)
        when (item.itemId) {
            R.id.bottomNavigationHome -> {
                if(homeFragment == null || !homeFragment.isVisible) {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.mainActivityFrameLayout, HomeFragment(), HOME_FRAGMENT_TAG_KEY)
                            .addToBackStack(null)
                            .commit()
                }
                setTitle(R.string.home_page)
                setMenuVisibility(true)
            }
            R.id.bottomNavigationSettings -> {
                if(settingsFragment == null || !settingsFragment.isVisible) {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.mainActivityFrameLayout, SettingsFragment(), SETTINGS_FRAGMENT_TAG_KEY)
                            .addToBackStack(null)
                            .commit()
                }
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

    override fun setMenuVisibility(isVisible: Boolean) {
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

    override fun onBackPressed() {
        val homeFragment = supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG_KEY)
        val settingsFragment = supportFragmentManager.findFragmentByTag(SETTINGS_FRAGMENT_TAG_KEY)

        super.onBackPressed()

        when {
            (homeFragment !== null && homeFragment.isVisible) ->{
                (mainActivityBottomNavigationView as BottomNavigationView).selectedItemId = R.id.bottomNavigationHome
            }
            (settingsFragment !== null && settingsFragment.isVisible) ->{
                (mainActivityBottomNavigationView as BottomNavigationView).selectedItemId = R.id.bottomNavigationSettings
            }
        }
    }
}
