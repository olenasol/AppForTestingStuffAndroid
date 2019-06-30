package com.testapp.appfortesting.screens.ipc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Messenger



class ConvertService:Service(){
    private val msg = Messenger(ConvertHandler())

    override fun onBind(intent: Intent?): IBinder? {
        return msg.binder
    }

}