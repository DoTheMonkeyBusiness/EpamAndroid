package com.example.epamandroid.mvp.views.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.example.epamandroid.R
import com.example.epamandroid.constants.MapConstants.LATITUDE_EXTRA_KEY
import com.example.epamandroid.constants.MapConstants.LONGITUDE_EXTRA_KEY
import com.example.epamandroid.constants.PermissionsConstants.LOCATION_PERMISSION_EXTRA_KEY
import com.example.epamandroid.constants.PermissionsConstants.READ_EXTERNAL_STORAGE_PERMISSION_EXTRA_KEY
import com.example.epamandroid.mvp.contracts.IAddLostDogContract
import com.example.epamandroid.mvp.presenters.AddLostDogPresenter
import com.example.epamandroid.mvp.views.fragments.ChooseLostBreedFragment
import com.example.filename.ImageFilePath
import com.example.kotlinextensions.goneView
import com.example.kotlinextensions.visibleView
import kotlinx.android.synthetic.main.activity_add_lost_dog.*
import java.io.File
import java.io.IOException


class AddLostDogActivity : AppCompatActivity(),
    IAddLostDogContract.View,
    ChooseLostBreedFragment.ISetLostBreedCallback {
    companion object {

        private const val TAG: String = "AddLostDogActivity"

        private const val IMAGE_FILE_TYPE_KEY: String = "image/*"
        private const val SELECT_PICTURE_INTENT_KEY: String = "Select Picture"
        private const val PICK_IMAGE_REQUEST_KEY: Int = 22
        private const val DEFAULT_DOUBLE_VALUE_KEY: Double = 0.0
    }

    private var imageFile: File? = null
    private var imageBitmap: Bitmap? = null
    private var readExternalStoragePermissionsGranted: Boolean = false
    private var imageSeted: Boolean = false
    private var isDownload: Boolean = false

    private lateinit var addLostDogActivityPresenter: IAddLostDogContract.Presenter

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
        setContentView(R.layout.activity_add_lost_dog)

        configureSupportActionBar()
        getReadExternalStoragePermission()

        addLostDogActivityPresenter = AddLostDogPresenter(this)

        addLostDogAddPhotoButton.setOnClickListener {
            choosePhoto()
        }

        addLostDogConfirmButton.setOnClickListener {
            if (!isDownload) {
                onConfirm()
            }
        }
    }

    private fun configureSupportActionBar() {
        setSupportActionBar(addLostDogCustomActionBarLayout as Toolbar?)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.add_lost_dog)
    }

    private fun getReadExternalStoragePermission() {
        val permissions: Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (this@AddLostDogActivity.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_DENIED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, READ_EXTERNAL_STORAGE_PERMISSION_EXTRA_KEY)
        } else {
            readExternalStoragePermissionsGranted = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        readExternalStoragePermissionsGranted = false

        when (requestCode) {
            LOCATION_PERMISSION_EXTRA_KEY -> {
                readExternalStoragePermissionsGranted =
                    grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            }
        }
    }

    private fun choosePhoto() {
        val intent = Intent()
        intent.apply {
            type = IMAGE_FILE_TYPE_KEY
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, SELECT_PICTURE_INTENT_KEY), PICK_IMAGE_REQUEST_KEY)
    }

    override fun imageUploadError() {
        Toast.makeText(applicationContext, getString(R.string.image_upload_error), Toast.LENGTH_LONG).show()
    }

    private fun onConfirm() {
        if (!imageSeted
            || addLostDogSelectADogTextView.text.isEmpty()
            || addLostEnterPhoneNumberEditText.text.isEmpty()
            || addLostDogAddBreedDescriptionEditText.text.isEmpty()
        ) {
            Toast.makeText(applicationContext, getString(R.string.all_fields_must_be_filled), Toast.LENGTH_LONG).show()
        } else {
            isDownload = true
            addLostDogActivityPresenter.uploadLostDog(
                addLostDogSelectADogTextView.text.toString(),
                addLostEnterPhoneNumberEditText.text.toString(),
                addLostDogAddBreedDescriptionEditText.text.toString(),
                intent.getDoubleExtra(LATITUDE_EXTRA_KEY, DEFAULT_DOUBLE_VALUE_KEY),
                intent.getDoubleExtra(LONGITUDE_EXTRA_KEY, DEFAULT_DOUBLE_VALUE_KEY),
                imageFile
            )
        }
    }

    override fun postSuccess() {
        finish()
    }

    override fun postError() {
        isDownload = false
        Toast.makeText(applicationContext, getString(R.string.announcement_posting_error), Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_KEY
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null
        ) {
            try {
                imageFile = File(ImageFilePath.getPath(this@AddLostDogActivity, data.data))
                imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                addLostDogAddPhotoImageView.setImageBitmap(imageBitmap)
                imageSeted = true
            } catch (e: IOException) {
                Toast.makeText(applicationContext, getString(R.string.photo_processing_error), Toast.LENGTH_LONG).show()
                imageSeted = false
            }
        }
    }

    override fun onSelectDogBreed(breed: String) {
        addLostDogSelectADogTextView.text = breed
    }
}
