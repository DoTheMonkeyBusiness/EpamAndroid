package com.example.epamandroid.views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.home_fragment.*

class SettingsFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(settingsFragmentCustomActionBarLayout as android.support.v7.widget.Toolbar?)
            setTitle(R.string.settings)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }
}