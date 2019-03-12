package com.example.epamandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import kotlinx.android.synthetic.main.activity_podcasts.my_toolbar

class PodcastsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcasts)

        configureSupportActionBar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun configureSupportActionBar() {
        setSupportActionBar(my_toolbar as Toolbar?)
        supportActionBar?.title = ""
        supportActionBar?.setIcon(R.drawable.ic_search_grey_dark_24dp)
    }

}