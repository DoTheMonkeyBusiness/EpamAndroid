package com.example.epamandroid.mvp.views.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import com.example.epamandroid.constants.FragmentConstants.HOME_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.constants.FragmentConstants.SETTINGS_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.R
import com.example.epamandroid.constants.FragmentConstants.MAP_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IMainActivityContract
import com.example.epamandroid.mvp.core.IBaseView
import com.example.epamandroid.mvp.presenters.MainActivityPresenter
import com.example.epamandroid.mvp.views.fragments.CameraFragment
import com.example.epamandroid.mvp.views.fragments.MainFragment
import com.example.epamandroid.mvp.views.adapters.ViewPagerAdapter
import com.example.epamandroid.mvp.views.fragments.HomeFragment
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo
import com.example.kotlinextensions.collapseBottomSheet
import com.example.kotlinextensions.expandBottomSheet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.breed_description.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

class MainActivity : AppCompatActivity(),
    MainFragment.IChangeFragmentMainItemCallback,
    CameraFragment.IChangeFragmentCameraItemCallback,
    HomeFragment.IShowBottomSheetCallback,
    CameraFragment.IShowBottomSheetCallback,
    IBaseView,
    IMainActivityContract.View {

    companion object {
        private const val CAMERA_ITEM_KEY: Int = 0
        private const val MAIN_ITEM_KEY: Int = 1
        private const val VIEW_PAGER_EXTRA_KEY: Int = 0
        private const val INTERNAL_FRAGMENTS_EXTRA_KEY: Int = 1
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_KEY: Int = 1212
    }

    private val viewPagerHistory: Stack<Int> = Stack()
    private val mainOrInternalHistory: Stack<Int> = Stack()

    private var isSaveToHistory: Boolean = false
    private var currentPage: Int = 1
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private var mainActivityPresenter: IMainActivityContract.Presenter? = null

    private lateinit var michelangelo: IMichelangelo

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

        checkExternalStoragePermission()

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

        bottomSheetBehavior = BottomSheetBehavior.from(breedDescriptionBottomSheet)

        isSaveToHistory = true

        breedDescriptionCloseButton.setOnClickListener {
            bottomSheetBehavior.collapseBottomSheet()
        }

        bottomSheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) = Unit

            override fun onStateChanged(p0: View, p1: Int) {
                when (bottomSheetBehavior?.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        hideError()
                        hideProgress()
                        hideDescription()
                    }
                }
            }
        })

        mainActivityPresenter = MainActivityPresenter(this)
        michelangelo = Michelangelo.getInstance(applicationContext)
    }

    override fun onBackPressed() {
        val homeFragment = supportFragmentManager
            .findFragmentByTag(HOME_FRAGMENT_TAG_EXTRA_KEY)
        val settingsFragment = supportFragmentManager
            .findFragmentByTag(SETTINGS_FRAGMENT_TAG_EXTRA_KEY)
        val mapFragment = supportFragmentManager
            .findFragmentByTag(MAP_FRAGMENT_TAG_EXTRA_KEY)

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
                    (mapFragment !== null && mapFragment.isVisible) -> {
                        (mainFragmentBottomNavigationView as BottomNavigationView)
                            .selectedItemId = R.id.bottomNavigationMap
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

    private fun checkExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_PERMISSION_KEY
            )
        }
    }

    override fun updateBreedDescription(dogEntity: DogEntity?) {
        if (dogEntity != null) {
            breedDescription.updateDogInfo(dogEntity)
            michelangelo.load(breedDescription.getDogPhoto(), dogEntity.photo)
            hideProgress()
            hideError()
            showDescription()
        } else {
            hideProgress()
            hideDescription()
            showError()
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

    private fun hideProgress() {
        if (breedDescriptionProgressBar.visibility != View.GONE) {
            breedDescriptionProgressBar.visibility = View.GONE
        }
    }

    private fun showProgress() {
        if (breedDescriptionProgressBar.visibility != View.VISIBLE) {
            breedDescriptionProgressBar.visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        if (breedDescriptionError.visibility != View.GONE) {
            breedDescriptionError.visibility = View.GONE
        }
    }

    private fun showError() {
        if (breedDescriptionError.visibility != View.VISIBLE) {
            breedDescriptionError.visibility = View.VISIBLE
        }
    }

    private fun hideDescription() {
        if (breedDescription.visibility != View.GONE) {
            breedDescription.visibility = View.GONE
        }
    }

    private fun showDescription() {
        if (breedDescription.visibility != View.VISIBLE) {
            breedDescription.visibility = View.VISIBLE
        }
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

    override fun onShowBottomSheetFromHome(dogEntity: DogEntity?) {
        bottomSheetBehavior.expandBottomSheet()
        if (dogEntity !== null) {
            breedDescription.updateDogInfo(dogEntity)
            michelangelo.load(breedDescription.getDogPhoto(), dogEntity.photo)
            hideError()
            hideProgress()
            showDescription()
        } else {
            showError()
        }
    }

    override fun onShowBottomSheetFromCamera(dogBreed: String) {
        bottomSheetBehavior.expandBottomSheet()
        showProgress()
        mainActivityPresenter?.loadDogByBreed(dogBreed)
    }
}
