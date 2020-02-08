package com.muklas.rungon.ui.play

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.muklas.rungon.MainActivity
import com.muklas.rungon.R
import com.muklas.rungon.model.Song
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : AppCompatActivity() {

    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        receiverSetup()
    }

    private fun receiverSetup() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                updateSong(intent.getParcelableExtra("song"))
            }
        }

        val intentFilter = IntentFilter(MainActivity.ACTION_SHOW)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun updateSong(song: Song?){
        song?.let {
            tvTitle.text = song.title
            tvArtist.text = song.artist
            tvLength.text = song.duration.toString()
            Glide.with(this).load(song.path).into(civAlbum)
        }
    }
}
