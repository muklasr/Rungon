package com.muklas.rungon.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import androidx.core.app.NotificationCompat
import com.muklas.rungon.MainActivity
import com.muklas.rungon.R
import java.io.IOException
import java.lang.ref.WeakReference

class MediaPlayerService : Service(), MediaPlayerCallback {

    private var mediaPlayer: MediaPlayer? = null
    private var isReady: Boolean = false

    companion object {
        const val ACTION_CREATE = "com.muklas.rungon.CREATE"
        const val ACTION_DESTROY = "com.muklas.rungon.DESTROY"
        const val PLAY = 0
        const val STOP = 1
        const val CHANGE = 2
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mMessenger.binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (!action.isNullOrEmpty()) {
            when (action) {
                ACTION_CREATE -> if (mediaPlayer == null) init()
                ACTION_DESTROY -> if (mediaPlayer?.isPlaying as Boolean) stopSelf()
                else -> init()
            }
        }
        return flags
    }

    private fun init() {
        mediaPlayer = MediaPlayer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val attribute = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            mediaPlayer?.setAudioAttributes(attribute)
        } else {
            mediaPlayer?.run {
                @Suppress("DEPRECATION")
                setAudioStreamType(AudioManager.STREAM_MUSIC)
            }
        }
    }

    override fun onChange(song: String) {
        onStop()
        mediaPlayer?.reset()
        try {
            mediaPlayer?.setDataSource(song)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mediaPlayer?.setOnPreparedListener {
            isReady = true
            it?.start()
            showNotification()
        }
        mediaPlayer?.setOnErrorListener { _, _, _ -> false }
        onStart()
    }

    override fun onStart() {
        if (!isReady) {
            mediaPlayer?.prepareAsync()
        } else {
            if (mediaPlayer?.isPlaying as Boolean) {
                mediaPlayer?.pause()
            } else {
                mediaPlayer?.start()
                showNotification()
            }
        }
    }

    override fun onStop() {
        if (mediaPlayer?.isPlaying as Boolean || isReady) {
            mediaPlayer?.stop()
            isReady = false
            stopNotification()
        }
    }

    private val mMessenger = Messenger(IncomingHandler(this))

    internal class IncomingHandler(playerCallback: MediaPlayerCallback) : Handler() {
        private val mediaPlayerCallbackReference: WeakReference<MediaPlayerCallback> =
            WeakReference(playerCallback)

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                PLAY -> mediaPlayerCallbackReference.get()?.onStart()
                STOP -> mediaPlayerCallbackReference.get()?.onStop()
                CHANGE -> mediaPlayerCallbackReference.get()?.onChange(msg.obj.toString())
                else -> super.handleMessage(msg)
            }
        }
    }

    private fun showNotification() {
        val channelDefaultImportance = "rungon_channel"
        val ongoingNotificationId = 1

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, channelDefaultImportance)
            .setContentTitle("Rungon")
            .setContentText("text")
            .setSmallIcon(R.drawable.ic_music_note_black_24dp)
            .setContentIntent(pendingIntent)
            .setTicker("ticker")
            .build()

        createChannel(channelDefaultImportance)
        startForeground(ongoingNotificationId, notification)
    }

    private fun createChannel(CHANNEL_ID: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT)
            channel.setShowBadge(false)
            channel.setSound(null, null)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun stopNotification() {
        stopForeground(false)
    }
}