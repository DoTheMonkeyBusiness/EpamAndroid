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
import kotlinx.android.synthetic.main.vk_photos_item.vk_photos_item_photos_count_textView
import kotlinx.android.synthetic.main.vk_photos_item.vk_photos_item_photo_gallery_linearLayout

class VkHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vk_home)

        configureSupportActionBar()

        vk_whatsnew_item_user_image.setImageDrawable(circleImage())

        vk_photos_item_photos_count_textView.text = vk_photos_item_photo_gallery_linearLayout.childCount.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun configureSupportActionBar() {
        setSupportActionBar(activity_vk_home_toolbar as Toolbar?)
        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setTitle(R.string.name)
        }
    }

    private fun circleImage() : RoundedBitmapDrawable {
        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.photo_vk)
        val roundedBitmapDrawable: RoundedBitmapDrawable

        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap).apply {
            isCircular = true
        }

        return roundedBitmapDrawable
    }
}