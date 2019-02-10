package com.testapp.appfortesting.screens.exoplayer.video_ads


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.ads.AdsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.fragment_video_with_ads.*


class VideoWithAdsFragment : Fragment() {

    var simpleExoPlayer : SimpleExoPlayer? = null
    val videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
    val vmapFileUri = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpost&cmsid=496&vid=short_onecue&correlator="
    lateinit var imaAdsLoader: ImaAdsLoader

    companion object {
        fun newInstance():VideoWithAdsFragment{
            return VideoWithAdsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_with_ads, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imaAdsLoader = ImaAdsLoader(context, Uri.parse(vmapFileUri))
    }

    override fun onStart() {
        super.onStart()
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context,
            DefaultTrackSelector()
        )
        playerView.player = simpleExoPlayer

        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(
            context,resources.getString(R.string.app_name)))
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(videoUrl))
        val adsMediaSource = AdsMediaSource(mediaSource,dataSourceFactory,
            imaAdsLoader, playerView.overlayFrameLayout)
        simpleExoPlayer!!.prepare(adsMediaSource)
        simpleExoPlayer!!.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        playerView.player = null
        simpleExoPlayer?.release()
        simpleExoPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        imaAdsLoader.release()
    }
}
