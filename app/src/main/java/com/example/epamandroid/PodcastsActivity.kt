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
        private const val NEW_EPISODES_KEY: String = "New episodes"
        private const val IN_PROGRESS_KEY: String = "In progress"
        private const val DOWNLOADS_KEY: String = "Downloads"
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
        with(adapter){
            let {
                it.addFragment(NewEpisodesFragment(), NEW_EPISODES_KEY)
                it.addFragment(InProgressFragment(), IN_PROGRESS_KEY)
                it.addFragment(DownloadsFragment(), DOWNLOADS_KEY)
            }
        }
        podcasts_for_you_item_viewpager.adapter = adapter
        podcasts_for_you_item_tab_layout.setupWithViewPager(podcasts_for_you_item_viewpager)
    }
}