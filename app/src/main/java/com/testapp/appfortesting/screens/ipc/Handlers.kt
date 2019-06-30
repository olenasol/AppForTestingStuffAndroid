package com.testapp.appfortesting.screens.ipc

import android.os.Handler
import android.os.Bundle
import android.os.Message
import android.os.RemoteException


class ConvertHandler:Handler(){

    companion object{
        public const val TO_UPPER_CASE = 1
        public const val TO_UPPER_CASE_RESPONSE = 2
    }

    override fun handleMessage(msg: Message) {
        // This is the action
        val msgType = msg.what

        when (msgType) {
            TO_UPPER_CASE -> {
                try {
                    // Incoming data
                    val data = msg.data.getString("data")
                    val resp = Message.obtain(null, TO_UPPER_CASE_RESPONSE)
                    val bResp = Bundle()
                    bResp.putString("respData", data?.toUpperCase())
                    resp.data = bResp

                    msg.replyTo.send(resp)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

            }
            else -> super.handleMessage(msg)
        }
    }
}
