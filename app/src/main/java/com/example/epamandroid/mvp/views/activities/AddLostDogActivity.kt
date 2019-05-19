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
import com.example.epamandroid.constants.MapConstants.LATITUDE_EXTRA_KEY
import com.example.epamandroid.constants.MapConstants.LONGITUDE_EXTRA_KEY
import com.example.epamandroid.constants.PermissionsConstants.LOCATION_PERMISSION_EXTRA_KEY
import com.example.epamandroid.constants.PermissionsConstants.READ_EXTERNAL_STORAGE_PERMISSION_EXTRA_KEY
import com.example.epamandroid.mvp.contracts.IAddLostDogContract
import com.example.epamandroid.mvp.presenters.AddLostDogPresenter
import com.example.epamandroid.mvp.views.fragments.ChooseLostBreedFragment
import kotlinx.android.synthetic.main.activity_add_lost_dog.*
import java.io.IOException


class AddLostDogActivity : AppCompatActivity(),
        IAddLostDogContract.View,
        ChooseLostBreedFragment.ISetLostBreedCallback {

    companion object {

        private const val TAG: String = "AddLostDogActivity"
        private const val IMAGE_FILE_TYPE_KEY: String = "image/*"
        private const val SELECT_PICTURE_INTENT_KEY: String = "Select Picture"
        private const val PICK_IMAGE_REQUEST_KEY: Int = 22
    }

    private var filePath: Uri? = null

    private var imageBitmap: Bitmap? = null
    private var readExternalStoragePermissionsGranted: Boolean = false
    private var imageSeted: Boolean = false

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
            onConfirm()
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

    private fun onConfirm() {
        if(!imageSeted
                || addLostDogSelectADogTextView.text.isEmpty()
                || addLostEnterPhoneNumberEditText.text.isEmpty()
                || addLostDogAddBreedDescriptionEditText.text.isEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.all_fields_must_be_filled), Toast.LENGTH_LONG).show()
        } else {
            addLostDogActivityPresenter.uploadLostDog(addLostDogSelectADogTextView.text.toString(),
                    addLostEnterPhoneNumberEditText.text.toString(),
                    addLostDogAddBreedDescriptionEditText.text.toString(),
                    intent.getDoubleExtra(LATITUDE_EXTRA_KEY, 0.0),
                    intent.getDoubleExtra(LONGITUDE_EXTRA_KEY, 0.0),
                    "photo")
        }
    }

//    private fun getBitmap(): ByteArray {
//        val bitmap = (addLostDogAddPhotoImageView.drawable as BitmapDrawable).bitmap
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        return baos.toByteArray()
//    }
//
//    private fun uploadImage() {
//        val path: ByteArray
//        val uploadFilePath = filePath
//
//        if (uploadFilePath != null){
//            path = getBitmap()
//
//            Thread {
//
//                try {
//                    val uploadId = UUID.randomUUID().toString()
//
//                    val req = MultipartBuilder().type(MultipartBuilder.FORM)
//                            .addFormDataPart(uploadId, "Path", RequestBody.create(MediaType.parse("headers: { \"Content-Type\":\"image/*\" },mode: \"cors\",cache: \"default\""), path))
//                            .build()
//
//                    val request = Request.Builder()
//                            .url("https://firebasestorage.googleapis.com/v0/b/dogbreeds-60b2e/o/myBucket%myBucket%2FImage?alt=media")
//                            .post(req)
//                            .build()
//                    val client = OkHttpClient()
//
//                    val response = client.newCall(request).execute()
//                    Log.d(TAG, response.body().string())
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }.start()
//        }
//    }

    override fun onPostSuccess() {
        finish()
    }

    override fun onPostError() {
        Toast.makeText(applicationContext, getString(R.string.announcement_posting_error), Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_KEY
                && resultCode == Activity.RESULT_OK) {
            filePath = data?.data

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
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
