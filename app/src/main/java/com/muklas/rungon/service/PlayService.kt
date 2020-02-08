package com.muklas.rungon.service

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.os.Parcelable
import com.muklas.rungon.MainActivity

class PlayService : IntentService("PlayService") {

    override fun onHandleIntent(intent: Intent?) {
        val i = Intent(MainActivity.ACTION_SHOW)
        i.putExtra("song", intent?.getParcelableExtra("song") as Parcelable)
        sendBroadcast(i)
    }
}
