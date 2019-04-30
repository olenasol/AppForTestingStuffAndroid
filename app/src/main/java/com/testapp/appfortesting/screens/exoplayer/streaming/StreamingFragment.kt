package com.testapp.appfortesting.screens.exoplayer.streaming


import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.decoder.DecoderCounters
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_streaming.*
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import java.io.IOException
import java.lang.Exception


class StreamingFragment : Fragment() {

    private val hlsVideoUri = "http://playertest.longtailvideo.com/adaptive/bbbfull/bbbfull.m3u8"
    private val rtmpVideoUri = "rtmp://mediacontrol.jargon.com.ar/elecotv/elecotv"
    private val dashVideoUri = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd"
    private val smoothStreamingVideoUri = "https://playready.directtaps.net/smoothstreaming/SSWSS720H264/SuperSpeedway_720.ism"

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    private var player: SimpleExoPlayer? = null


    enum class StreamType {
        HLS, RTMP, DASH, SMOOTH_STREAMING
    }

    companion object {

        const val ARGS_STREAM_TYPE = "args_stream_type"

        fun newInstance(streamType: StreamType): StreamingFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARGS_STREAM_TYPE, streamType)
            val fragment = StreamingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.testapp.appfortesting.R.layout.fragment_streaming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        player = getSimpleExoplayer()
        initListeners()

        playerView.player = player
        val mediaSource = getMediaSource()
        player?.prepare(mediaSource)
        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare(mediaSource, true, false)


    }

    private fun initListeners() {
        player?.addAnalyticsListener(object : AnalyticsListener{
            override fun onSeekProcessed(eventTime: AnalyticsListener.EventTime?) {

            }

            override fun onPlaybackParametersChanged(
                eventTime: AnalyticsListener.EventTime?,
                playbackParameters: PlaybackParameters?
            ) {

            }

            override fun onPlayerError(eventTime: AnalyticsListener.EventTime?, error: ExoPlaybackException?) {
                Toast.makeText(context,"playerError",Toast.LENGTH_LONG).show()
            }

            override fun onSeekStarted(eventTime: AnalyticsListener.EventTime?) {
            }

            override fun onLoadingChanged(eventTime: AnalyticsListener.EventTime?, isLoading: Boolean) {
            }

            override fun onDownstreamFormatChanged(
                eventTime: AnalyticsListener.EventTime?,
                mediaLoadData: MediaSourceEventListener.MediaLoadData?
            ) {
            }

            override fun onDrmKeysLoaded(eventTime: AnalyticsListener.EventTime?) {
            }

            override fun onMediaPeriodCreated(eventTime: AnalyticsListener.EventTime?) {
            }

            override fun onRenderedFirstFrame(eventTime: AnalyticsListener.EventTime?, surface: Surface?) {
            }

            override fun onReadingStarted(eventTime: AnalyticsListener.EventTime?) {
            }

            override fun onBandwidthEstimate(
                eventTime: AnalyticsListener.EventTime?,
                totalLoadTimeMs: Int,
                totalBytesLoaded: Long,
                bitrateEstimate: Long
            ) {
                Toast.makeText(context,"bitrate estimate = "+bitrateEstimate,Toast.LENGTH_SHORT).show()
            }


            override fun onPlayerStateChanged(
                eventTime: AnalyticsListener.EventTime?,
                playWhenReady: Boolean,
                playbackState: Int
            ) {
            }

            override fun onDrmKeysRestored(eventTime: AnalyticsListener.EventTime?) {
            }

            override fun onDecoderDisabled(
                eventTime: AnalyticsListener.EventTime?,
                trackType: Int,
                decoderCounters: DecoderCounters?
            ) {
            }

            override fun onShuffleModeChanged(eventTime: AnalyticsListener.EventTime?, shuffleModeEnabled: Boolean) {
            }

            override fun onDecoderInputFormatChanged(
                eventTime: AnalyticsListener.EventTime?,
                trackType: Int,
                format: Format?
            ) {
            }

            override fun onAudioSessionId(eventTime: AnalyticsListener.EventTime?, audioSessionId: Int) {
            }

            override fun onDrmSessionManagerError(eventTime: AnalyticsListener.EventTime?, error: Exception?) {
            }

            override fun onLoadStarted(
                eventTime: AnalyticsListener.EventTime?,
                loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                mediaLoadData: MediaSourceEventListener.MediaLoadData?
            ) {
            }

            override fun onTracksChanged(
                eventTime: AnalyticsListener.EventTime?,
                trackGroups: TrackGroupArray?,
                trackSelections: TrackSelectionArray?
            ) {}

            override fun onPositionDiscontinuity(eventTime: AnalyticsListener.EventTime?, reason: Int) {
            }

            override fun onRepeatModeChanged(eventTime: AnalyticsListener.EventTime?, repeatMode: Int) {
            }

            override fun onUpstreamDiscarded(
                eventTime: AnalyticsListener.EventTime?,
                mediaLoadData: MediaSourceEventListener.MediaLoadData?
            ) {
            }

            override fun onLoadCanceled(
                eventTime: AnalyticsListener.EventTime?,
                loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                mediaLoadData: MediaSourceEventListener.MediaLoadData?
            ) {
            }

            override fun onMediaPeriodReleased(eventTime: AnalyticsListener.EventTime?) {
            }

            override fun onTimelineChanged(eventTime: AnalyticsListener.EventTime?, reason: Int) {
            }

            override fun onDecoderInitialized(
                eventTime: AnalyticsListener.EventTime?,
                trackType: Int,
                decoderName: String?,
                initializationDurationMs: Long
            ) {}

            override fun onDroppedVideoFrames(
                eventTime: AnalyticsListener.EventTime?,
                droppedFrames: Int,
                elapsedMs: Long
            ) {
                Toast.makeText(context,"Number of dropped frames ="+droppedFrames,Toast.LENGTH_SHORT ).show()
            }

            override fun onDecoderEnabled(
                eventTime: AnalyticsListener.EventTime?,
                trackType: Int,
                decoderCounters: DecoderCounters?
            ) {
            }

            override fun onVideoSizeChanged(
                eventTime: AnalyticsListener.EventTime?,
                width: Int,
                height: Int,
                unappliedRotationDegrees: Int,
                pixelWidthHeightRatio: Float
            ) {
            }

            override fun onAudioUnderrun(
                eventTime: AnalyticsListener.EventTime?,
                bufferSize: Int,
                bufferSizeMs: Long,
                elapsedSinceLastFeedMs: Long
            ) {
            }

            override fun onLoadCompleted(
                eventTime: AnalyticsListener.EventTime?,
                loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                mediaLoadData: MediaSourceEventListener.MediaLoadData?
            ) {
            }

            override fun onDrmKeysRemoved(eventTime: AnalyticsListener.EventTime?) {
            }

            override fun onLoadError(
                eventTime: AnalyticsListener.EventTime?,
                loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                mediaLoadData: MediaSourceEventListener.MediaLoadData?,
                error: IOException?,
                wasCanceled: Boolean
            ) {
            }

            override fun onMetadata(eventTime: AnalyticsListener.EventTime?, metadata: Metadata?) {
            }

        })
        player?.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {

            }

            override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {

            }

            override fun onLoadingChanged(isLoading: Boolean) {

            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> loading.visibility = View.GONE
                    Player.STATE_BUFFERING -> loading.visibility = View.VISIBLE
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {

            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

            }

            override fun onPlayerError(error: ExoPlaybackException) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onPositionDiscontinuity(reason: Int) {

            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

            }

            override fun onSeekProcessed() {

            }
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (player!!.playbackState != Player.STATE_READY) {
            player!!.playWhenReady = true
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (player!!.playbackState == Player.STATE_READY)
            player!!.playWhenReady = false

    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player?.currentPosition!!
            currentWindow = player?.currentWindowIndex!!
            playWhenReady = player?.playWhenReady!!
            player?.release()
            player = null
        }
    }

    private fun getSimpleExoplayer(): SimpleExoPlayer? {

        val adaptiveTrackSelection = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
        return ExoPlayerFactory.newSimpleInstance(
            context,
            DefaultRenderersFactory(context),
            DefaultTrackSelector(adaptiveTrackSelection)
        )


    }

    private fun getMediaSource(): MediaSource? {
        val defaultBandwidthMeter = DefaultBandwidthMeter()
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "Exo2"), defaultBandwidthMeter
        )
        when (arguments?.getSerializable(ARGS_STREAM_TYPE)) {
            StreamType.HLS -> {
                return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(hlsVideoUri))
            }
            StreamType.RTMP -> {
                return ExtractorMediaSource.Factory(RtmpDataSourceFactory())
                    .createMediaSource(Uri.parse(rtmpVideoUri))
            }
            StreamType.DASH ->{
                val manifestDataSourceFactory = DefaultHttpDataSourceFactory("ua")
                val dashChunkSourceFactory = DefaultDashChunkSource.Factory(DefaultHttpDataSourceFactory("ua", defaultBandwidthMeter))
                return DashMediaSource.Factory(dashChunkSourceFactory,manifestDataSourceFactory)
                    .createMediaSource(Uri.parse(dashVideoUri))
            }
            StreamType.SMOOTH_STREAMING ->{
                return SsMediaSource.Factory(DefaultSsChunkSource.Factory(dataSourceFactory), dataSourceFactory)
                    .createMediaSource(Uri.parse(smoothStreamingVideoUri))
            }
            else -> {
                return ExtractorMediaSource.Factory(RtmpDataSourceFactory())
                    .createMediaSource(Uri.parse(rtmpVideoUri))
            }
        }
    }

}
