package com.example.epamandroid

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.simple_fragment.simple_fragment_hello_button

class SimpleFragment : Fragment() {

    companion object {
        private const val BUTTON_TEXT_KEY: String = "buttonText"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            simple_fragment_hello_button.text = savedInstanceState.getString(BUTTON_TEXT_KEY)
        }

        simple_fragment_hello_button.setOnClickListener {
            if (simple_fragment_hello_button.text == getText(R.string.hello_button)) {
                simple_fragment_hello_button.setText(R.string.smile_button)
            } else {
                simple_fragment_hello_button.setText(R.string.hello_button)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(BUTTON_TEXT_KEY, simple_fragment_hello_button.text.toString())
    }
}


