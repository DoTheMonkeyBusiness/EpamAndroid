package com.example.epamandroid.mvp.views.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.epamandroid.constants.FragmentConstants.HOME_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.constants.FragmentConstants.SETTINGS_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.R
import com.example.epamandroid.constants.FragmentConstants.MAP_FRAGMENT_TAG_EXTRA_KEY
import com.example.kotlinextensions.changeFragment
import com.example.kotlinextensions.changeFragmentWithBackStack
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val TITLE_KEY: String = "titleKey"
        private const val ACTIONBAR_ITEMS_VISIBILITY_KEY: String = "actionbarItemsVisibilityKey"
        private const val IS_SWIPE_PAGING_ENABLED_KEY: String = "actionbarItemsVisibilityKey"
    }

    private var isVisibleMenuItem: Boolean = true
    private var isSwipePagingEnabled: Boolean = true

    private var callback: IChangeFragmentMainItemCallback? = null

    private lateinit var mainActivity: AppCompatActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainActivity = (activity as AppCompatActivity)

        mainActivity.apply {
            setSupportActionBar(mainFragmentCustomActionBarLayout as Toolbar?)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_photo_camera)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        setHasOptionsMenu(true)
        setMenuVisibility(false)

        when {
            (savedInstanceState == null) -> {
                mainActivity.apply {
                    changeFragment(R.id.mainFragmentFrameLayout,
                            HomeFragment(), HOME_FRAGMENT_TAG_EXTRA_KEY)
                    setTitle(R.string.home_page)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    isVisibleMenuItem = true
                    setMenuVisibility(isVisibleMenuItem)
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
                isVisibleMenuItem = savedInstanceState
                        .getBoolean(ACTIONBAR_ITEMS_VISIBILITY_KEY)
            }
        }

        (mainFragmentBottomNavigationView as BottomNavigationView)
                .setOnNavigationItemSelectedListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IChangeFragmentMainItemCallback) {
            callback = context
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val homeFragment = activity
                ?.supportFragmentManager
                ?.findFragmentByTag(HOME_FRAGMENT_TAG_EXTRA_KEY)
        val settingsFragment = activity
                ?.supportFragmentManager
                ?.findFragmentByTag(SETTINGS_FRAGMENT_TAG_EXTRA_KEY)
        val mapFragment = activity
                ?.supportFragmentManager
                ?.findFragmentByTag(MAP_FRAGMENT_TAG_EXTRA_KEY)

        when (item.itemId) {
            R.id.bottomNavigationHome -> {

                if (homeFragment == null
                        || !homeFragment.isVisible) {

                    mainActivity
                            .changeFragmentWithBackStack(R.id.mainFragmentFrameLayout,
                                    HomeFragment(), HOME_FRAGMENT_TAG_EXTRA_KEY)
                }

                mainActivity.apply {
                    setTitle(R.string.home_page)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }

                isVisibleMenuItem = true
                setMenuVisibility(isVisibleMenuItem)
                isSwipePagingEnabled = true
                callback?.onViewPagerSwipePagingEnabled(isSwipePagingEnabled)
            }
            R.id.bottomNavigationMap -> {

                if (mapFragment == null
                        || !mapFragment.isVisible) {

                    mainActivity
                            .changeFragmentWithBackStack(R.id.mainFragmentFrameLayout,
                                    MapFragment(), MAP_FRAGMENT_TAG_EXTRA_KEY)
                }

                mainActivity.apply {
                    setTitle(R.string.lost_dogs)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }

                isVisibleMenuItem = false
                setMenuVisibility(isVisibleMenuItem)
                isSwipePagingEnabled = false
                callback?.onViewPagerSwipePagingEnabled(isSwipePagingEnabled)
            }
            R.id.bottomNavigationSettings -> {

                if (settingsFragment == null
                        || !settingsFragment.isVisible) {

                    mainActivity
                            .changeFragmentWithBackStack(R.id.mainFragmentFrameLayout,
                                    SettingsFragment(), SETTINGS_FRAGMENT_TAG_EXTRA_KEY)
                }

                mainActivity.apply {
                    setTitle(R.string.settings)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }

                isVisibleMenuItem = false
                setMenuVisibility(isVisibleMenuItem)
                isSwipePagingEnabled = false
                callback?.onViewPagerSwipePagingEnabled(isSwipePagingEnabled)
            }
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.home_action_bar_menu, menu)
        setMenuVisibility(isVisibleMenuItem)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(TITLE_KEY, mainActivity.title.toString())
        outState.putBoolean(IS_SWIPE_PAGING_ENABLED_KEY, isSwipePagingEnabled)
        outState.putBoolean(ACTIONBAR_ITEMS_VISIBILITY_KEY, isVisibleMenuItem)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem?): Boolean {
        val id = menuItem?.itemId

        if (id == android.R.id.home) {
            callback?.onItemChangedToCamera()
        }

        return super.onOptionsItemSelected(menuItem)
    }

    interface IChangeFragmentMainItemCallback {
        fun onItemChangedToCamera()
        fun onItemChangedToInternalFragment()
        fun onViewPagerSwipePagingEnabled(changeSwipePagingEnabled: Boolean)
    }
}