package com.example.epamandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import kotlinx.android.synthetic.main.activity_podcasts.my_toolbar
import kotlinx.android.synthetic.main.podcasts_for_you_item.podcasts_for_you_item_viewpager
import kotlinx.android.synthetic.main.podcasts_for_you_item.podcasts_for_you_item_tab_layout

class PodcastsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcasts)

        configureSupportActionBar()

        configureTabLayout()
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

    private fun configureTabLayout() {
        val adapter = ForYouPagerAdapter(supportFragmentManager)

        adapter.addFragment(NewEpisodesFragment(), "New episodes")
        adapter.addFragment(InProgressFragment(), "In progress")
        adapter.addFragment(DownloadsFragment(), "Downloads")
        podcasts_for_you_item_viewpager.adapter = adapter
        podcasts_for_you_item_tab_layout.setupWithViewPager(podcasts_for_you_item_viewpager)

    }
}