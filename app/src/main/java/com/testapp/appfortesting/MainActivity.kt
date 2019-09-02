package com.testapp.appfortesting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.testapp.appfortesting.screens.barcode.BarcodeFragment
import com.testapp.appfortesting.screens.constraintlayout.ConstraintStatesFragment
import com.testapp.appfortesting.screens.content_provider.ContactsFragment
import com.testapp.appfortesting.screens.drawing.DrawingFragment
import com.testapp.appfortesting.screens.exoplayer.streaming.StreamingFragment
import com.testapp.appfortesting.screens.rv_header.RVHeaderFragment
import com.testapp.appfortesting.screens.webview.WebViewFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fresco.initialize(this)
        supportFragmentManager.beginTransaction().replace(
            R.id.mainContent,
            RVHeaderFragment.newInstance()
        ).commit()
    }
}
