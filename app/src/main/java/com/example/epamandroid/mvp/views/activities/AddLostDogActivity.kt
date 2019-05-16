package com.example.epamandroid.mvp.views.activities

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.activity_add_lost_dog.*

class AddLostDogActivity : AppCompatActivity() {

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
    }

    private fun configureSupportActionBar() {
        setSupportActionBar(addLostDogCustomActionBarLayout as Toolbar?)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.add_lost_dog)
    }
}