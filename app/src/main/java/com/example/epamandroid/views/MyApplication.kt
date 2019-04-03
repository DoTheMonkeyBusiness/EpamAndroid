package com.example.epamandroid.views

import android.app.Application
import android.preference.PreferenceManager
import com.example.epamandroid.R

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val isDarkModeEnabled = PreferenceManager.getDefaultSharedPreferences(this)

        if (isDarkModeEnabled.getBoolean(getString(R.string.switch_day_night_mode_key), false)) {
            setTheme(R.style.AppThemeNight)
        } else {
            setTheme(R.style.AppThemeDay)
        }
    }
}