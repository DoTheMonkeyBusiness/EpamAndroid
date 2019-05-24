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
import com.example.epamandroid.constants.DogEntityConstants.AFFECTIONATE_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.BREED_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.BREED_POPULARITY_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.CAN_LIVE_AT_HOME_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.COST_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.DESCRIPTION_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.DOG_ENTITY_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.HEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.ID_EXTRA_KEY
import com.example.epamandroid.constants.DogEntityConstants.WEIGHT_EXTRA_KEY
import com.example.epamandroid.constants.FragmentConstants.DOG_BREED_DESCRIPTION_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.constants.FragmentConstants.LOST_DOG_BREED_DESCRIPTION_FRAGMENT_TAG_EXTRA_KEY
import com.example.epamandroid.constants.PermissionsConstants.WRITE_EXTERNAL_STORAGE_PERMISSION_EXTRA_KEY
import com.example.epamandroid.models.DogEntity
import com.example.epamandroid.mvp.contracts.IMainActivityContract
import com.example.epamandroid.mvp.core.IBaseView
import com.example.epamandroid.mvp.presenters.MainActivityPresenter
import com.example.epamandroid.mvp.views.adapters.ViewPagerAdapter
import com.example.epamandroid.mvp.views.fragments.*
import com.example.imageloader.IMichelangelo
import com.example.imageloader.Michelangelo
import com.example.kotlinextensions.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.breed_description_fragment.*
import kotlinx.android.synthetic.main.main_bottom_sheet.*

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

        bottomSheetBehavior = BottomSheetBehavior.from(mainBottomSheet)

        mainBottomSheetCloseButton.setOnClickListener {
            bottomSheetBehavior.collapseBottomSheet()
        }

        bottomSheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) = Unit

            override fun onStateChanged(p0: View, p1: Int) {
                when (bottomSheetBehavior?.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        mainBottomSheetError.goneView()
                        mainBottomSheetProgressBar.goneView()
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
                    WRITE_EXTERNAL_STORAGE_PERMISSION_EXTRA_KEY
            )
        }
    }

    override fun updateBreedDescription(dogEntity: DogEntity?) {
        if (dogEntity != null) {
            breedDescription.updateDogInfo(dogEntity)
            michelangelo.load(breedDescription.getDogPhoto(), dogEntity.photo)
            mainBottomSheetProgressBar.goneView()
            mainBottomSheetError.goneView()
            breedDescription.visibleView()
        } else {
            mainBottomSheetProgressBar.goneView()
            breedDescription.goneView()
            mainBottomSheetError.visibleView()
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

    private fun replaceBottomSheetContainer(fragmentKey: String, dogEntity: DogEntity? = null) {
        val bundle = Bundle()
//        if(dogEntity != null) {
//            dogEntity.id?.let { bundle.putInt(ID_EXTRA_KEY, it) }
//            dogEntity.breed?.let { bundle.putString(BREED_EXTRA_KEY, it) }
//            dogEntity.weight?.let { bundle.putString(WEIGHT_EXTRA_KEY, it) }
//            dogEntity.height?.let { bundle.putString(HEIGHT_EXTRA_KEY, it) }
//            dogEntity.description?.let { bundle.putString(DESCRIPTION_EXTRA_KEY, it) }
//            dogEntity.isCanLiveAtHome?.let { bundle.putBoolean(CAN_LIVE_AT_HOME_EXTRA_KEY, it) }
//            dogEntity.isAffectionate?.let { bundle.putBoolean(AFFECTIONATE_EXTRA_KEY, it) }
//            dogEntity.breedPopularity?.let { bundle.putFloat(BREED_POPULARITY_EXTRA_KEY, it) }
//            dogEntity.cost?.let { bundle.putInt(COST_EXTRA_KEY, it) }
//            dogEntity.lifeExpectancy?.let { bundle.putInt(COST_EXTRA_KEY, it) }
//        }
        bundle.putParcelable(DOG_ENTITY_EXTRA_KEY, dogEntity)
        when (fragmentKey) {
            DOG_BREED_DESCRIPTION_FRAGMENT_TAG_EXTRA_KEY -> {
                val breedDescriptionFragment = BreedDescriptionFragment()
                breedDescriptionFragment.arguments = bundle
                changeFragment(R.id.mainBottomSheetFragmentContainer, breedDescriptionFragment, fragmentKey)
            }
            LOST_DOG_BREED_DESCRIPTION_FRAGMENT_TAG_EXTRA_KEY -> changeFragment(R.id.mainBottomSheetFragmentContainer, LostBreedDescriptionFragment(), fragmentKey)
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

    override fun onViewPagerSwipePagingEnabled(changeSwipePagingEnabled: Boolean) {
        activityMainViewPager
                .setSwipePagingEnabled(changeSwipePagingEnabled)
    }

    override fun onShowBottomSheetFromHome(dogEntity: DogEntity?) {
        replaceBottomSheetContainer(DOG_BREED_DESCRIPTION_FRAGMENT_TAG_EXTRA_KEY, dogEntity)
        bottomSheetBehavior.expandBottomSheet()
        if (dogEntity !== null) {
            mainBottomSheetError.goneView()
            mainBottomSheetProgressBar.goneView()
        } else {
            mainBottomSheetError.visibleView()
        }
    }

    override fun onShowBottomSheetFromCamera(dogBreed: String) {
        replaceBottomSheetContainer(DOG_BREED_DESCRIPTION_FRAGMENT_TAG_EXTRA_KEY)
        bottomSheetBehavior.expandBottomSheet()
        mainBottomSheetProgressBar.visibleView()
        mainActivityPresenter?.loadDogByBreed(dogBreed)
    }
}
