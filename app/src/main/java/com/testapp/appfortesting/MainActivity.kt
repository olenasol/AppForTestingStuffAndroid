package com.testapp.appfortesting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.testapp.appfortesting.screens.constraintlayout.ConstraintStatesFragment
import com.testapp.appfortesting.screens.exoplayer.playlist.PlaylistFragment
import com.testapp.appfortesting.screens.exoplayer.streaming.StreamingFragment
import com.testapp.appfortesting.screens.ipc.IPCMessangerFragment
import com.testapp.appfortesting.screens.navview.NavigationFragment
import com.testapp.appfortesting.screens.request_graphql.GraphqlFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fresco.initialize(this)
        supportFragmentManager.beginTransaction().replace(
            R.id.mainContent,
            NavigationFragment()
        ).commit()
    }
}
