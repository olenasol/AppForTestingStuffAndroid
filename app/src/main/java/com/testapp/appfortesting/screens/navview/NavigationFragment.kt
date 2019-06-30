package com.testapp.appfortesting.screens.navview

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.fragment_nav.*


class NavigationFragment:Fragment(){

    companion object {
        fun newInstance(): NavigationFragment {
            return NavigationFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawerLayout.setScrimColor(Color.TRANSPARENT)
        drawerLayout.openDrawer(navView)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newOrientation = newConfig.orientation

        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else{
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
        }
        drawerLayout.openDrawer(navView)
    }
}