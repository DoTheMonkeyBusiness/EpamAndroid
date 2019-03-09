package com.example.epamandroid

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import kotlinx.android.synthetic.main.activity_vk_home.activity_vk_home_toolbar
import kotlinx.android.synthetic.main.vk_whatsnew_item.vk_whatsnew_item_user_image

class VkHomeActivity : AppCompatActivity() {

    private lateinit var bitmap: Bitmap
    private lateinit var roundedBitmapDrawable: RoundedBitmapDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vk_home)

        configureSupportActionBar()

        createCircleImage()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun configureSupportActionBar() {
        setSupportActionBar(activity_vk_home_toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.name)
    }

    private fun createCircleImage() {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.photo)
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmapDrawable.isCircular = true
        vk_whatsnew_item_user_image.setImageDrawable(roundedBitmapDrawable)
    }
}