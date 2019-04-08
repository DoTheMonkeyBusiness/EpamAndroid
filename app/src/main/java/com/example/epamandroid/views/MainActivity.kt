package com.example.epamandroid.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import com.example.epamandroid.Constants.HOME_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.Constants.SETTINGS_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainActivity : AppCompatActivity(), MainFragment.IChangeFragmentMainItemCallback, CameraFragment.IChangeFragmentCameraItemCallback {

    companion object {
        private const val CAMERA_ITEM_KEY: Int = 0
        private const val MAIN_ITEM_KEY: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val isDarkModeEnabled = PreferenceManager.getDefaultSharedPreferences(this)

        if (isDarkModeEnabled.getBoolean(
                        getString(R.string.switch_day_night_mode_key), false)) {

            setTheme(R.style.AppThemeNight)
        } else {
            setTheme(R.style.AppThemeDay)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureViewPager()

        if (savedInstanceState == null) {
            activityMainViewPager.setCurrentItem(MAIN_ITEM_KEY, true)
        }

    }

    override fun onBackPressed() {
        val homeFragment = supportFragmentManager
                .findFragmentByTag(HOME_FRAGMENT_TAG_EXTRA_KEY)
        val settingsFragment = supportFragmentManager
                .findFragmentByTag(SETTINGS_FRAGMENT_TAG_EXTRA_KEY)

        super.onBackPressed()

        when {
            (homeFragment !== null && homeFragment.isVisible) -> {
                (mainFragmentBottomNavigationView as BottomNavigationView)
                        .selectedItemId = R.id.bottomNavigationHome
            }
            (settingsFragment !== null && settingsFragment.isVisible) -> {
                (mainFragmentBottomNavigationView as BottomNavigationView)
                        .selectedItemId = R.id.bottomNavigationSettings
            }
        }
    }

    private fun configureViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
                .apply {
                    let {
                        it.addFragment(CameraFragment())
                        it.addFragment(MainFragment())
                    }
                }

        activityMainViewPager.adapter = adapter
    }

    override fun onFragmentMainItemChanged() {
        activityMainViewPager
                .setCurrentItem(CAMERA_ITEM_KEY, true)
    }

    override fun onFragmentCameraItemChanged() {
        activityMainViewPager
                .setCurrentItem(MAIN_ITEM_KEY, true)
    }

    override fun onViewPagerSwipePagingEnabled(changeSwipePagingEnabled: Boolean) {
        activityMainViewPager
                .setSwipePagingEnabled(changeSwipePagingEnabled)
    }

}
