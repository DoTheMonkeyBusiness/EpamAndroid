package com.example.epamandroid.views

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.epamandroid.Constants.HOME_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.Constants.SETTINGS_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val TITLE_KEY: String = "titleKey"
        private const val ACTIONBAR_ITEMS_VISIBILITY_KEY: String = "actionbarItemsVisibilityKey"
        private const val IS_SWIPE_PAGING_ENABLED_KEY: String = "actionbarItemsVisibilityKey"
    }

    private var isVisibleMenuItem: Boolean = true
    private var isSwipePagingEnabled: Boolean = true

    private var callback: IChangeViewItemCallback? = null

    private lateinit var mainActivity: AppCompatActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainActivity = (activity as AppCompatActivity)

        mainActivity.apply {
            setSupportActionBar(mainFragmentCustomActionBarLayout as Toolbar?)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_photo_camera)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        when {
            (savedInstanceState == null) -> {
                mainActivity.apply {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.mainFragmentFrameLayout, HomeFragment(), HOME_FRAGMENT_TAG_EXTRA_KEY)
                            .commit()
                    setTitle(R.string.home_page)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }

                isVisibleMenuItem = true
                callback?.onViewPagerSwipePagingEnabled(isSwipePagingEnabled)
            }
            else -> {
                mainActivity.apply {
                    title = savedInstanceState.getString(TITLE_KEY)
                    supportActionBar
                            ?.setDisplayHomeAsUpEnabled(
                                    savedInstanceState
                                            .getBoolean(ACTIONBAR_ITEMS_VISIBILITY_KEY))
                }

                callback
                        ?.onViewPagerSwipePagingEnabled(
                                savedInstanceState
                                        .getBoolean(IS_SWIPE_PAGING_ENABLED_KEY))
            }
        }

        setHasOptionsMenu(true)

        (mainFragmentBottomNavigationView as BottomNavigationView)
                .setOnNavigationItemSelectedListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IChangeViewItemCallback) {
            callback = context
        }
    }

    override fun onResume() {
        super.onResume()

        isVisibleMenuItem = when {
            (mainActivity.title == getString(R.string.home_page)) -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val homeFragment = activity
                ?.supportFragmentManager
                ?.findFragmentByTag(HOME_FRAGMENT_TAG_EXTRA_KEY)
        val settingsFragment = activity
                ?.supportFragmentManager
                ?.findFragmentByTag(SETTINGS_FRAGMENT_TAG_EXTRA_KEY)

        when (item.itemId) {
            R.id.bottomNavigationHome -> {

                if (homeFragment == null
                        || !homeFragment.isVisible) {

                    mainActivity
                            .supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.mainFragmentFrameLayout, HomeFragment(), HOME_FRAGMENT_TAG_EXTRA_KEY)
                            .addToBackStack(null)
                            .commit()
                }

                mainActivity.apply {
                    setTitle(R.string.home_page)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }

                isMenuVisible(true)
                isSwipePagingEnabled = true
                callback?.onViewPagerSwipePagingEnabled(isSwipePagingEnabled)
            }
            R.id.bottomNavigationSettings -> {

                if (settingsFragment == null
                        || !settingsFragment.isVisible) {

                    mainActivity
                            .supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.mainFragmentFrameLayout, SettingsFragment(), SETTINGS_FRAGMENT_TAG_EXTRA_KEY)
                            .addToBackStack(null)
                            .commit()
                }

                mainActivity.apply {
                    setTitle(R.string.settings)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }

                isMenuVisible(false)
                isSwipePagingEnabled = false
                callback?.onViewPagerSwipePagingEnabled(isSwipePagingEnabled)
            }
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.home_action_bar_menu, menu)
        isMenuVisible(isVisibleMenuItem)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(TITLE_KEY, mainActivity.title.toString())
        outState.putBoolean(IS_SWIPE_PAGING_ENABLED_KEY, isSwipePagingEnabled)
    }

    private fun isMenuVisible(isVisible: Boolean) {
        (mainFragmentCustomActionBarLayout as Toolbar?)
                ?.menu
                ?.getItem(0)
                ?.isVisible = isVisible
    }

    override fun onOptionsItemSelected(menuItem: MenuItem?): Boolean {
        val id = menuItem?.itemId

        if (id == android.R.id.home) {
            callback?.onViewPagerItemChanged(0, true)
        }

        return super.onOptionsItemSelected(menuItem)
    }

    interface IChangeViewItemCallback {
        fun onViewPagerItemChanged(item: Int, smoothScroll: Boolean)
        fun onViewPagerSwipePagingEnabled(changeSwipePagingEnabled: Boolean)
    }
}