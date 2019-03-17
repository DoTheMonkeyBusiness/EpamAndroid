package com.example.epamandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import kotlinx.android.synthetic.main.activity_podcasts.my_toolbar
import kotlinx.android.synthetic.main.podcasts_for_you_item.podcasts_for_you_item_viewpager
import kotlinx.android.synthetic.main.podcasts_for_you_item.podcasts_for_you_item_tab_layout

class PodcastsActivity : AppCompatActivity() {

    companion object {
        private const val EMPTY_LINE_KEY: String = ""
    }

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
        supportActionBar?.apply {
            title = EMPTY_LINE_KEY
            setIcon(R.drawable.ic_search_grey_dark)
        }
    }

    private fun configureTabLayout() {
        val adapter = ForYouPagerAdapter(supportFragmentManager)

        adapter.let {
                it.addFragment(NewEpisodesFragment(), "New episodes")
                it.addFragment(InProgressFragment(), "In progress")
                it.addFragment(DownloadsFragment(), "Downloads")
        }
        podcasts_for_you_item_viewpager.let {
            it.adapter = adapter
        }
        podcasts_for_you_item_tab_layout.let {
            it.setupWithViewPager(podcasts_for_you_item_viewpager)
        }
    }
}