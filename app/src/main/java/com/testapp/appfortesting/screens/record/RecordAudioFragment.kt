package com.testapp.appfortesting.screens.record


import android.content.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.testapp.appfortesting.R
import android.media.MediaRecorder
import android.os.Environment
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.fragment_record_audio.*


class RecordAudioFragment : Fragment() {

    private var mediaRecorder: MediaRecorder? = null
   // private var mediaPlayer: MediaPlayer? = null
    private val fileName = Environment.getExternalStorageDirectory().toString() + "/record.3gpp"
    private var isRecording: Boolean = false
    //private var isPlaying: Boolean = false

    private var mService: PlayerForegroundService? = null
    // Tracks the bound state of the service.
    private var mBound = false
    // Monitors the state of the connection to the service.
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as PlayerForegroundService.LocalBinder
            mService = binder.getService()
            mBound = true
            mService?.let {
                togglePlayState(it.isPlaying)
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }

    val playReceiver = object : BroadcastReceiver() {

        override fun onReceive( context:Context, intent:Intent? ) {
            val isPlayFromNotification = intent?.extras?.getBoolean(PlayerForegroundService.EXTRA_PAUSE_OR_PLAY)
            if(isPlayFromNotification != null ) {
 //               isPlaying = isPlayFromNotification
                togglePlayState(isPlayFromNotification)
            }
        }
    };

    companion object {
        fun newInstance(): RecordAudioFragment {
            return RecordAudioFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_audio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPlayerService()
        btnRecord.setOnClickListener {
            if(!isRecording){
                releaseRecorder()
                initMediaRecorder()
                isRecording = true
                btnRecord.setImageResource(R.drawable.ic_mic)
            } else{
                mediaRecorder?.stop()
                isRecording = false
                btnRecord.setImageResource(R.drawable.ic_mic_off)
            }
        }
        btnPlay.setOnClickListener {
            mService?.togglePlayState()
            mService?.let {
                togglePlayState(it.isPlaying)
            }
        }
    }

    private fun startPlayerService(){
        val intent = Intent(activity, PlayerForegroundService::class.java)
        intent.action = PlayerForegroundService.ACTION_START_FOREGROUND_SERVICE
        activity?.startService(intent)
    }
    private fun stopPlayerService(){
        val intent = Intent(activity, PlayerForegroundService::class.java)
        intent.action = PlayerForegroundService.ACTION_STOP_FOREGROUND_SERVICE
        activity?.startService(intent)
    }

    private fun togglePlayState(isPlaying: Boolean){
        if (isPlaying){
            btnPlay.setImageResource(R.drawable.ic_pause)
        }else{
            btnPlay.setImageResource(R.drawable.ic_play_arrow)
        }
    }
    override fun onStart() {
        super.onStart()
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        context?.bindService(Intent(context, PlayerForegroundService::class.java), mServiceConnection,
            Context.BIND_AUTO_CREATE)
    }
    override fun onResume() {
        super.onResume()
        context?.let {
            LocalBroadcastManager.getInstance(it).apply {
                registerReceiver(playReceiver, IntentFilter(PlayerForegroundService.ACTION_SERVICE_TOGGLE))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        context?.let {
            LocalBroadcastManager.getInstance(it).apply {
                unregisterReceiver(playReceiver)
            }
        }
    }
    override fun onStop() {
        if (mBound) {
            context?.unbindService(mServiceConnection)
            mBound = false
        }
        super.onStop()
    }


    private fun initMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.setOutputFile(fileName)
        mediaRecorder?.prepare()
        mediaRecorder?.start()
    }


    private fun releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder?.release()
            mediaRecorder = null
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        releaseRecorder()
    }
}
