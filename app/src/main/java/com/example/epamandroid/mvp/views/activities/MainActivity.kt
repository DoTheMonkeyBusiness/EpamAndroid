package com.example.epamandroid.mvp.views.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import com.example.epamandroid.R
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
import com.example.kotlinextensions.goneView
import com.example.kotlinextensions.visibleView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.breed_description.*

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
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_KEY: Int = 1212
    }

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
                when (position) {
                    CAMERA_ITEM_KEY -> currentPage = CAMERA_ITEM_KEY
                    MAIN_ITEM_KEY -> currentPage = MAIN_ITEM_KEY
                }
            }

        })

        bottomSheetBehavior = BottomSheetBehavior.from(breedDescriptionBottomSheet)

        breedDescriptionCloseButton.setOnClickListener {
            bottomSheetBehavior.collapseBottomSheet()
        }

        bottomSheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) = Unit

            override fun onStateChanged(p0: View, p1: Int) {
                when (bottomSheetBehavior?.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        breedDescriptionError.goneView()
                        breedDescriptionProgressBar.goneView()
                        breedDescription.goneView()
                    }
                }
            }
        })

        mainActivityPresenter = MainActivityPresenter(this)
        michelangelo = Michelangelo.getInstance(applicationContext)
    }

    override fun onBackPressed() {
        if (currentPage == MAIN_ITEM_KEY) {
            super.onBackPressed()
        } else {
            activityMainViewPager
                .setCurrentItem(MAIN_ITEM_KEY, true)
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
            breedDescriptionProgressBar.goneView()
            breedDescriptionError.goneView()
            breedDescription.visibleView()
        } else {
            breedDescriptionProgressBar.goneView()
            breedDescription.goneView()
            breedDescriptionError.visibleView()
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

    override fun onViewPagerSwipePagingEnabled(changeSwipePagingEnabled: Boolean) {
        activityMainViewPager
            .setSwipePagingEnabled(changeSwipePagingEnabled)
    }

    override fun onShowBottomSheetFromHome(dogEntity: DogEntity?) {
        bottomSheetBehavior.expandBottomSheet()
        if (dogEntity !== null) {
            breedDescription.updateDogInfo(dogEntity)
            michelangelo.load(breedDescription.getDogPhoto(), dogEntity.photo)
            breedDescriptionError.goneView()
            breedDescriptionProgressBar.goneView()
            breedDescription.visibleView()
        } else {
            breedDescriptionError.visibleView()
        }
    }

    override fun onShowBottomSheetFromCamera(dogBreed: String) {
        bottomSheetBehavior.expandBottomSheet()
        breedDescriptionProgressBar.visibleView()
        mainActivityPresenter?.loadDogByBreed(dogBreed)
    }
}
