package com.example.epamandroid.mvp.views.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.example.epamandroid.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        val modeSwitcher = findPreference(getString(R.string.switch_day_night_mode_key))

        modeSwitcher.setOnPreferenceChangeListener { _, _ ->
            activity?.recreate()

            true
        }
    }
}