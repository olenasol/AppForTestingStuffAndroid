package com.testapp.appfortesting.screens.ipc


import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.testapp.appfortesting.R
import android.content.ServiceConnection
import android.os.*
import com.testapp.appfortesting.screens.ipc.ConvertHandler.Companion.TO_UPPER_CASE_RESPONSE
import kotlinx.android.synthetic.main.fragment_ipcmessanger.*
import android.os.Bundle
import android.os.Messenger
import com.testapp.appfortesting.screens.ipc.ConvertHandler.Companion.TO_UPPER_CASE


class IPCMessangerFragment : Fragment() {

    private var sConn: ServiceConnection? = null
    private var messenger: Messenger? = null

    fun newInstance():IPCMessangerFragment{
        return IPCMessangerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sConn = object: ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                messenger = null
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                messenger = Messenger(service)
            }
        }
        // We bind to the service
        activity?.bindService(
            Intent(activity, ConvertService::class.java), sConn!!,
            Context.BIND_AUTO_CREATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ipcmessanger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSend.setOnClickListener {
            val input = etInput.text.toString()
            val msg = Message
                .obtain(null, TO_UPPER_CASE)

            msg.replyTo = Messenger(ResponseHandler())
            // We pass the value
            val b = Bundle()
            b.putString("data", input)

            msg.data = b

            try {
                messenger?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    // This class handles the Service response
    @SuppressLint("HandlerLeak")
    inner class ResponseHandler : Handler() {

        override fun handleMessage(msg: Message) {
            val respCode = msg.what

            when (respCode) {
                TO_UPPER_CASE_RESPONSE -> {
                    val result = msg.data.getString("respData")
                    txtResult.text = result
                }
            }
        }

    }
}
