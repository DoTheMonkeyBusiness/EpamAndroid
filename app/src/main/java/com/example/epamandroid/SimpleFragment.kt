package com.example.epamandroid

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.simple_fragment.simple_fragment_hello_button

class SimpleFragment : Fragment() {

    private val BUTTON_TEXT: String = "buttonText"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //TODO: I little don't understand how it works
        if (savedInstanceState != null) {
            //TODO: FATAL EXCEPTION when turing the screen
            simple_fragment_hello_button.setText(savedInstanceState.getInt(BUTTON_TEXT))
        }

        simple_fragment_hello_button.setOnClickListener {
            if (simple_fragment_hello_button.text.equals(getText(R.string.hello_button))) {
                simple_fragment_hello_button.setText(R.string.smile_button)
            } else {
                simple_fragment_hello_button.setText(R.string.hello_button)
            }
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString(BUTTON_TEXT, simple_fragment_hello_button.text.toString())

        super.onSaveInstanceState(savedInstanceState)
    }
}


