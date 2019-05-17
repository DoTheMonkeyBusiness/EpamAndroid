package com.example.epamandroid.mvp.views.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.example.epamandroid.R
import com.example.epamandroid.constants.PermissionsConstants.LOCATION_PERMISSION_KEY
import com.example.epamandroid.constants.PermissionsConstants.READ_EXTERNAL_STORAGE_PERMISSION_KEY
import com.example.epamandroid.mvp.contracts.IAddLostDogContract
import kotlinx.android.synthetic.main.activity_add_lost_dog.*
import java.io.IOException

class AddLostDogActivity : AppCompatActivity(), IAddLostDogContract.View {

    companion object {
        private const val IMAGE_FILE_TYPE_KEY: String = "image/*"
        private const val SELECT_PICTURE_INTENT_KEY: String = "Select Picture"
        private const val PICK_IMAGE_REQUEST_KEY: Int = 22
    }

    private var filePath: Uri? = null
    private var imageBitmap: Bitmap? = null

    private var readExternalStoragePermissionsGranted: Boolean = false

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
        getLocationPermission()

        addLostDogAddPhotoButton.setOnClickListener {
            choosePhoto()
        }
    }

    private fun configureSupportActionBar() {
        setSupportActionBar(addLostDogCustomActionBarLayout as Toolbar?)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.add_lost_dog)
    }

    private fun getLocationPermission() {
        val permissions: Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (this@AddLostDogActivity.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_DENIED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, READ_EXTERNAL_STORAGE_PERMISSION_KEY)
        } else {
            readExternalStoragePermissionsGranted = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        readExternalStoragePermissionsGranted = false

        when (requestCode) {
            LOCATION_PERMISSION_KEY -> {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_KEY
            && resultCode == Activity.RESULT_OK) {
            filePath = data?.data

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                addLostDogAddPhotoImageView.setImageBitmap(imageBitmap)
            } catch (e: IOException) {
                Toast.makeText(applicationContext, getString(R.string.photo_processing_error), Toast.LENGTH_LONG).show()
            }
        }
    }
}