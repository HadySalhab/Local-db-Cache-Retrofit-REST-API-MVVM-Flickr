package com.android.flickphoto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.android.flickphoto.ui.display.DisplayPhotoFragment
import com.android.flickphoto.ui.li.PhotoListFragment
import com.android.flickphoto.ui.preferences.FlickrPhotoSettings
import com.android.flickphoto.util.PreferencesStorage
import com.google.android.material.appbar.AppBarLayout


private const val TAG="PhotoListActivity"
class MainActivity : AppCompatActivity() , DisplayPhotoFragment.Callbacks , FlickrPhotoSettings.Callbacks , PhotoListFragment.Callbacks {
    private lateinit var toolbar:Toolbar
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var appBarLayout:AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpScreenMode()

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        appBarLayout = findViewById(R.id.main_appbarlayout)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfig = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController,appBarConfig)
    }

    private fun setUpScreenMode() {

        val mode = PreferencesStorage.getStoredScreenMode(this)
            when (mode) {
            getString(R.string.night_mode) -> {
                updateScreenMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            getString(R.string.light_mode) -> {
                updateScreenMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            getString(R.string.system_mode) -> {
                updateScreenMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    override fun showToolbar() {
        //toolbar can be collapsed from the list Fragment, before display fragment is displayed
        //this callback ensure that the toolbar will always be visible(not collapsed) when display fragment is displayed
        appBarLayout.setExpanded(true,true)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,appBarConfig)
    }



    fun updateScreenMode(mode:Int){
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    override fun onSettingFragmentDisplayed() {
        //removing toolbar scroll behavior in the setting screen
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = 0
    }

    override fun onPhotoListFragmentDisplayed() {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.setScrollFlags(
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        )
    }
}
