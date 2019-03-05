package com.example.epamandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mysubmodule.Myclass
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Myclass.myFunc()
    }


    fun onButtonClick(view: View) {
        button1.setText(R.string.button1_new)
    }
}
