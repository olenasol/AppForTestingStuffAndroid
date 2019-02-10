package com.testapp.appfortesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.testapp.appfortesting.screens.rvdragdrop.linear.DragSwipeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.mainContent,
            DragSwipeFragment.newInstance()).commit()
    }
}
