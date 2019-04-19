package com.example.epamandroid.mvp.views.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import com.example.epamandroid.constants.FragmentConstants.HOME_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.constants.FragmentConstants.SETTINGS_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.R
import com.example.epamandroid.mvp.views.fragments.CameraFragment
import com.example.epamandroid.mvp.views.fragments.MainFragment
import com.example.epamandroid.mvp.views.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

class MainActivity : AppCompatActivity(), MainFragment.IChangeFragmentMainItemCallback,
        CameraFragment.IChangeFragmentCameraItemCallback {

    companion object {
        private const val CAMERA_ITEM_KEY: Int = 0
        private const val MAIN_ITEM_KEY: Int = 1
        private const val VIEW_PAGER_EXTRA_KEY: Int = 0
        private const val INTERNAL_FRAGMENTS_EXTRA_KEY: Int = 1
    }

    private val viewPagerHistory: Stack<Int> = Stack()
    private val mainOrInternalHistory: Stack<Int> = Stack()

    private var isSaveToHistory: Boolean = false
    private var currentPage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        val isDarkModeEnabled = PreferenceManager.getDefaultSharedPreferences(this)

        if (isDarkModeEnabled.getBoolean(
                getString(R.string.switch_day_night_mode_key), false
            )
        ) {

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

        activityMainViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) = Unit

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) = Unit

            override fun onPageSelected(position: Int) {
                if (isSaveToHistory) {
                    viewPagerHistory.push(currentPage)
                    mainOrInternalHistory.push((VIEW_PAGER_EXTRA_KEY))
                }
            }

        })

        isSaveToHistory = true
    }

    override fun onBackPressed() {
        val homeFragment = supportFragmentManager
            .findFragmentByTag(HOME_FRAGMENT_TAG_EXTRA_KEY)
        val settingsFragment = supportFragmentManager
            .findFragmentByTag(SETTINGS_FRAGMENT_TAG_EXTRA_KEY)

        when {
            (viewPagerHistory.empty()
                    || (mainOrInternalHistory.pop() == INTERNAL_FRAGMENTS_EXTRA_KEY)) -> {
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
            else -> {
                isSaveToHistory = false
                activityMainViewPager.setCurrentItem(viewPagerHistory.pop(), true)
                isSaveToHistory = true
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

    override fun onItemChangedToCamera() {
        activityMainViewPager
            .setCurrentItem(CAMERA_ITEM_KEY, true)
    }

    override fun onItemChangedToMain() {
        activityMainViewPager
            .setCurrentItem(MAIN_ITEM_KEY, true)
    }

    override fun onItemChangedToInternalFragment() {
        mainOrInternalHistory.push(INTERNAL_FRAGMENTS_EXTRA_KEY)
    }

    override fun onViewPagerSwipePagingEnabled(changeSwipePagingEnabled: Boolean) {
        activityMainViewPager
            .setSwipePagingEnabled(changeSwipePagingEnabled)
    }
}
