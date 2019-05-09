package com.testapp.appfortesting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.testapp.appfortesting.screens.constraintlayout.ConstraintStatesFragment
import com.testapp.appfortesting.screens.exoplayer.streaming.StreamingFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fresco.initialize(this)
        supportFragmentManager.beginTransaction().replace(
            R.id.mainContent,
            ConstraintStatesFragment.newInstance()
        ).commit()
    }
}
