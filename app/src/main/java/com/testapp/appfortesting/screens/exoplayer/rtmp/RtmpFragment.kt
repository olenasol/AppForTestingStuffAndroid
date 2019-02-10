package com.testapp.appfortesting.screens.exoplayer.rtmp


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.fragment_rtmp.*

class RtmpFragment : Fragment() {

    private var player: SimpleExoPlayer? = null

    companion object {
        fun newInstance(): RtmpFragment {
            return RtmpFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rtmp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlayer()
    }

    fun initPlayer(){
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        //Create the player
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        playerView.player = player

        val rtmpDataSourceFactory = RtmpDataSourceFactory()
        // This is the MediaSource representing the media to be played.
        val videoSource = ExtractorMediaSource.Factory(rtmpDataSourceFactory)
            .createMediaSource(Uri.parse("rtmp://mediacontrol.jargon.com.ar/elecotv/elecotv"))

        // Prepare the player with the source.
        player!!.prepare(videoSource)
        //auto start playing
        player!!.playWhenReady = true
    }

    override fun onResume() {
        super.onResume()
        if (player!!.playbackState != Player.STATE_READY)
            player!!.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        if (player!!.playbackState == Player.STATE_READY)
            player!!.playWhenReady = false
    }
}
