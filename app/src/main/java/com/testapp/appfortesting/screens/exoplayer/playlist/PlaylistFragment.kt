package com.testapp.appfortesting.screens.exoplayer.playlist


import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.fragment_playlist.*
import org.opencv.android.Utils


class PlaylistFragment : Fragment() {

    private var exoPlayer:ExoPlayer? = null
    private var trackSelector:DefaultTrackSelector? = null
    private var shoulAutoPlay:Boolean = true

    private var mainHandler:Handler? = null
    private var bandwidthMeter:BandwidthMeter? = null

    private val videos = ArrayList<VideoModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createDemoPlaylist()
        mainHandler = Handler()
        bandwidthMeter = DefaultBandwidthMeter()
    }

    private fun createDemoPlaylist() {

            videos.add(VideoModel("https://fpdl.vimeocdn.com/vimeo-prod-skyfire-std-us/01/2148/8/210743998/722741417.mp4?token=1557968087-0x8b6fd9e7730207a13b84c12752663af77e870708"))
            videos.add(VideoModel("https://gcs-vimeo.akamaized.net/exp=1557969449~acl=%2A%2F1135811463.mp4%2A~hmac=12ea1cf83e08893ef6ad54aac92a09b2f1c7193063e0b2c1a4f28f3c35aaf378/vimeo-prod-skyfire-std-us/01/4585/11/297927791/1135811463.mp4"))
        videos.add(VideoModel("https://fpdl.vimeocdn.com/vimeo-prod-skyfire-std-us/01/963/11/279818380/1048609001.mp4?token=1557969453-0x1b04356857e60f870662de885dfeac86c4d8bbdf"))
        videos.add(VideoModel("https://gcs-vimeo.akamaized.net/exp=1557969456~acl=%2A%2F1062516367.mp4%2A~hmac=72a12b5dac65598d93cb9a0be6ae822bd06c907f192fd3cbadd5fc9c6ddd8cba/vimeo-prod-skyfire-std-us/01/1551/11/282759939/1062516367.mp4"))
    }
    private fun builMediaSource( uri: Uri):MediaSource{
        val dataSource = DefaultDataSourceFactory(context,Util.getUserAgent(context,"PlayList"))
        val mediaSource = ExtractorMediaSource.Factory(dataSource).createMediaSource(uri)
        return mediaSource
    }

    private fun initPlayer(){
        simplePlayerView.requestFocus()
        val videoTrackSelector = AdaptiveTrackSelection.Factory(bandwidthMeter)
        trackSelector = DefaultTrackSelector(videoTrackSelector)
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context,trackSelector)
        simplePlayerView.player = exoPlayer

        val mediaSources= arrayOfNulls<MediaSource>(videos.size)
        for (i in 0 until videos.size){
            mediaSources[i] = builMediaSource(Uri.parse(videos[i].videoUrl))
        }
        val mediaSource = ConcatenatingMediaSource(*mediaSources)
        exoPlayer?.prepare(mediaSource)
    }

    override fun onStart() {
        super.onStart()
        initPlayer()
    }

    private fun release(){
        shoulAutoPlay = exoPlayer!!.playWhenReady
        exoPlayer?.release()
        exoPlayer = null
        trackSelector = null
    }

    override fun onPause() {
        super.onPause()
        release()
    }
}
