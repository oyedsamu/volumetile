package com.oyedsamu.volumetile

import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.app.Activity
import android.widget.SeekBar

class MainActivity : Activity() {

    private val handler = Handler(Looper.getMainLooper())
    private val dismissRunnable = Runnable { finish() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val audio = getSystemService(AUDIO_SERVICE) as AudioManager
        val max = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        val icon = findViewById<ImageView>(R.id.volumeIcon)
        val seekBar = findViewById<SeekBar>(R.id.volumeSeekBar)

        seekBar.max = max
        seekBar.progress = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        updateIcon(icon, seekBar.progress, max)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                    updateIcon(icon, progress, max)
                }
            }
            override fun onStartTrackingTouch(sb: SeekBar) = handler.removeCallbacks(dismissRunnable)
            override fun onStopTrackingTouch(sb: SeekBar) = scheduleDismiss()
        })

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
    }
}
