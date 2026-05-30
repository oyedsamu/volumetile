package com.oyedsamu.volumetile

import android.app.Activity
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView

class MainActivity : Activity() {

    private val handler = Handler(Looper.getMainLooper())
    private val dismissRunnable = Runnable { finish() }
    private var toneGen: ToneGenerator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val audio = getSystemService(AUDIO_SERVICE) as AudioManager
        val max = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        val icon = findViewById<ImageView>(R.id.volumeIcon)
        val seekBar = findViewById<VerticalSeekBar>(R.id.volumeSeekBar)

        seekBar.max = max
        seekBar.progress = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        updateIcon(icon, seekBar.progress, max)

        seekBar.listener = object : VerticalSeekBar.OnChangeListener {
            override fun onProgressChanged(progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                    updateIcon(icon, progress, max)
                    // Recreate ToneGenerator at new volume level so the beep reflects it
                    val volumePct = if (max > 0) (progress * 100 / max) else 0
                    toneGen?.release()
                    toneGen = try {
                        ToneGenerator(AudioManager.STREAM_MUSIC, volumePct)
                    } catch (e: RuntimeException) { null }
                }
            }
            override fun onStartTracking() = handler.removeCallbacks(dismissRunnable)
            override fun onStopTracking() {
                // Play a short beep at the chosen volume so the user hears the level
                toneGen?.startTone(ToneGenerator.MIN_VOLUME, ToneGenerator.MAX_VOLUME)
                scheduleDismiss()
            }
        }

        findViewById<android.view.View>(R.id.backgroundDim).setOnClickListener { finish() }

        scheduleDismiss()
    }

    private fun updateIcon(icon: ImageView, volume: Int, max: Int) {
        icon.setImageResource(when {
            volume == 0 -> R.drawable.ic_volume_off
            volume < max / 2 -> R.drawable.ic_volume_down
            else -> R.drawable.ic_volume_up
        })
    }

    private fun scheduleDismiss() {
        handler.removeCallbacks(dismissRunnable)
        handler.postDelayed(dismissRunnable, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(dismissRunnable)
        toneGen?.release()
        toneGen = null
    }
}
