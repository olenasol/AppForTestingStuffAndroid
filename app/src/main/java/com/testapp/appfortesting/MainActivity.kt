package com.testapp.appfortesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.testapp.appfortesting.screens.camera2api.Camera2ApiFragment
import com.testapp.appfortesting.screens.exoplayer.rtmp.RtmpFragment
import com.testapp.appfortesting.screens.exoplayer.video_ads.VideoWithAdsFragment
import com.testapp.appfortesting.screens.maps.MapsFragment
import com.testapp.appfortesting.screens.motionlayout.MotionFragment
import com.testapp.appfortesting.screens.rvdragdrop.linear.DragSwipeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.mainContent,
            MotionFragment.newInstance()).commit()
    }
}
