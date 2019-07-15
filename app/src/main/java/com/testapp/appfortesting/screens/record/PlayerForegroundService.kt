package com.testapp.appfortesting.screens.record

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.PendingIntent
import android.app.Notification
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.fragment_record_audio.*


class PlayerForegroundService: Service(){

    private var mediaPlayer: MediaPlayer? = null
    private val fileName = Environment.getExternalStorageDirectory().toString() + "/record.3gpp"
    public var isPlaying: Boolean = false

    private val mBinder = LocalBinder()

    companion object {
        private val TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE"

        const val ACTION_SERVICE_TOGGLE = "ACTON_SERVICE_TOGGLE"

        const val EXTRA_PAUSE_OR_PLAY = "EXTRA_PAUSE_PLAY"

        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"

        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"

        const val ACTION_PAUSE = "ACTION_PAUSE"

        const val ACTION_PLAY = "ACTION_PLAY"

    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action

            when (action) {
                ACTION_START_FOREGROUND_SERVICE -> {
                    startForegroundService()
                    Toast.makeText(applicationContext, "Foreground service is started.", Toast.LENGTH_LONG).show()
                }
                ACTION_STOP_FOREGROUND_SERVICE -> {
                    releasePlayer()
                    stopForegroundService()
                    Toast.makeText(applicationContext, "Foreground service is stopped.", Toast.LENGTH_LONG).show()
                }
                ACTION_PLAY -> {
                    notifyFragmentPlay(true)
                    play()
                    Toast.makeText(applicationContext, "You click Play button.", Toast.LENGTH_LONG).show()}
                ACTION_PAUSE -> {
                    notifyFragmentPlay(false)
                    stop()
                    Toast.makeText(applicationContext, "You click Pause button.", Toast.LENGTH_LONG).show()}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    /* Used to build and start foreground service. */
    private fun startForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.")

        // Create notification default intent.
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        // Create notification builder.
        val builder = NotificationCompat.Builder(this)

        // Make notification show big text.
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle("Music player implemented by foreground service.")
        bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.")
        // Set big text style.
        builder.setStyle(bigTextStyle)

        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.mipmap.ic_launcher)
        val largeIconBitmap = BitmapFactory.decodeResource(resources, R.drawable.lviv)
        builder.setLargeIcon(largeIconBitmap)
        // Make the notification max priority.
        builder.priority = Notification.PRIORITY_MAX
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true)

        // Add Play button intent in notification.
        val playIntent = Intent(this, PlayerForegroundService::class.java)
        playIntent.action = ACTION_PLAY
        val pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0)
        val playAction = NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pendingPlayIntent)
        builder.addAction(playAction)

        // Add Pause button intent in notification.
        val pauseIntent = Intent(this, PlayerForegroundService::class.java)
        pauseIntent.action = ACTION_PAUSE
        val pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0)
        val prevAction = NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent)
        builder.addAction(prevAction)

        // Add Exit button intent in notification.
        val exitIntent = Intent(this, PlayerForegroundService::class.java)
        exitIntent.action = ACTION_STOP_FOREGROUND_SERVICE
        val pendingExitIntent = PendingIntent.getService(this, 0, exitIntent, 0)
        val exitAction = NotificationCompat.Action(android.R.drawable.ic_media_pause, "Exit", pendingExitIntent)
        builder.addAction(exitAction)

        // Build the notification.
        val notification = builder.build()

        // Start foreground service.
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    fun togglePlayState(){
        if (!isPlaying){
            releasePlayer()
            initMediaPlayer()
            isPlaying = true
        }else{
            mediaPlayer?.stop()
            isPlaying = false
        }
    }
    private fun play(){
        if(!isPlaying) {
            releasePlayer()
            initMediaPlayer()
            isPlaying = true
        }
    }
    private fun stop(){
        if(isPlaying){
            mediaPlayer?.stop()
            isPlaying = false
        }
    }
    private fun initMediaPlayer(){
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(fileName)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }
    private fun releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
    private fun stopForegroundService() {

        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.")
        stopForeground(true)
        stopSelf()
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        fun getService() = this@PlayerForegroundService
    }

    private fun notifyFragmentPlay(toggle: Boolean) {
        val intent = Intent(ACTION_SERVICE_TOGGLE)
        intent.putExtra(EXTRA_PAUSE_OR_PLAY, toggle)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

}