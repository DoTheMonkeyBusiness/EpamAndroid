package com.example.epamandroid.views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.support.v7.widget.Toolbar
import com.example.epamandroid.R
import kotlinx.android.synthetic.main.home_fragment.*
import android.support.v7.app.AppCompatActivity



class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

}