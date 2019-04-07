package com.example.epamandroid.views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.example.epamandroid.R

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }
}