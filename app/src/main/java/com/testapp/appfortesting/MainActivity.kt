package com.testapp.appfortesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.testapp.appfortesting.screens.opencv.OpenCVFragment
import com.testapp.appfortesting.screens.record.RecordAudioFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.mainContent,
            OpenCVFragment.newInstance()).commit()
    }
}
