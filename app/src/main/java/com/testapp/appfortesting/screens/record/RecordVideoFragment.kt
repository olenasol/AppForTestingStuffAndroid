package com.testapp.appfortesting.screens.record


import android.content.res.Configuration
import android.hardware.Camera
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup

import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.fragment_record_video.*
import java.io.IOException

class RecordVideoFragment : Fragment(), SurfaceHolder.Callback  {

    private var mSurfaceHolder: SurfaceHolder? = null
    private var mCamera: Camera? = null
    private var mediaRecorder: MediaRecorder? = null
    private val fileName = Environment.getExternalStorageDirectory().toString() + "/record.mp4"
    private var isRecording: Boolean = false

    override fun surfaceChanged(
        holder: SurfaceHolder, format: Int, width: Int,
        height: Int
    ) {
        mCamera?.stopPreview()
        if (mCamera != null) {
            try {
                mCamera?.setPreviewDisplay(mSurfaceHolder)
                mCamera?.startPreview()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mCamera = Camera.open()
        if (this.resources.configuration.orientation !== Configuration.ORIENTATION_LANDSCAPE) {
            mCamera?.setDisplayOrientation(90)
        } else {
            mCamera?.setDisplayOrientation(0)
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mCamera?.stopPreview()
        mCamera?.release()
        mCamera = null
    }

    companion object {
        fun newInstance(): RecordVideoFragment{
            return RecordVideoFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSurfaceHolder = surfaceView.holder
        mSurfaceHolder?.addCallback(this)
        btnRecord.setOnClickListener {
            initMediaRecorder()
        }
        btnStop.setOnClickListener {
            mediaRecorder?.stop()
        }
    }

    private fun initMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setPreviewDisplay(mSurfaceHolder?.surface)
//        mediaRecorder?.setOrientationHint(90)
        //mediaRecorder?.setCamera(mCamera)
        mediaRecorder?.setVideoSource(MediaRecorder.VideoSource.DEFAULT)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mediaRecorder?.setVideoEncodingBitRate(2500 * 1000);
        mediaRecorder?.setVideoFrameRate(30)
        mediaRecorder?.setVideoSize(640, 480);
        mediaRecorder?.setOutputFile(fileName)

        mediaRecorder?.prepare()
        mediaRecorder?.start()
    }

}
