package com.testapp.appfortesting.screens.record


import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.testapp.appfortesting.R
import android.media.MediaRecorder
import android.os.Environment
import kotlinx.android.synthetic.main.fragment_record_video.*


class RecordAudioFragment : Fragment() {

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private val fileName = Environment.getExternalStorageDirectory().toString() + "/record.3gpp"
    private var isRecording: Boolean = false
    private var isPlaying: Boolean = false

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
        return inflater.inflate(R.layout.fragment_record_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            if (!isPlaying){
                releasePlayer()
                initMediaPlayer()
                isPlaying = true
                btnPlay.setImageResource(R.drawable.ic_pause)
            }else{
                mediaPlayer?.stop()
                isPlaying = false
                btnPlay.setImageResource(R.drawable.ic_play_arrow)
            }
        }
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

    private fun initMediaPlayer(){
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(fileName)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }

    private fun releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder?.release()
            mediaRecorder = null
        }
    }

    private fun releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        releaseRecorder()
    }
}
