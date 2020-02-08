package com.muklas.rungon

import android.annotation.SuppressLint
import android.content.*
import android.os.*
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.muklas.rungon.model.Song
import com.muklas.rungon.service.MediaPlayerService
import com.muklas.rungon.ui.PlayFragment
import com.muklas.rungon.ui.play.ItemListDialogFragment
import com.muklas.rungon.ui.play.PlayActivity
import kotlinx.android.synthetic.main.action_bar.view.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var broadcastReceiver: BroadcastReceiver

    companion object {
        private lateinit var mBoundServiceIntent: Intent
        private var mService: Messenger? = null
        private var mServiceBound = false
        const val ACTION_SHOW = "action_show"

        private val mServiceConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                mService = null
                mServiceBound = false
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mService = Messenger(service)
                mServiceBound = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        actionBarSetup() //custom action bar setup
        receiverSetup() //register the receiver
        serviceSetup()
        changeMenu(1)

        btnPlay.setOnClickListener(this)
        btnPause.setOnClickListener(this)
        btnShow.setOnClickListener(this)
    }

    @SuppressLint("InflateParams")
    private fun actionBarSetup() {
        val actionBarLayout = layoutInflater.inflate(R.layout.action_bar, null) as ViewGroup
        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        with(supportActionBar) {
            this?.setDisplayHomeAsUpEnabled(false)
            this?.setDisplayShowTitleEnabled(false)
            this?.setDisplayShowCustomEnabled(true)
            this?.elevation = 0F
            this?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            this?.setCustomView(actionBarLayout, params)
            this?.customView?.let {
                customView.btnMenu.setOnClickListener(this@MainActivity)
                customView.btnMic.setOnClickListener(this@MainActivity)
                customView.btnOptions.setOnClickListener(this@MainActivity)
                customView.btnAddPlaylist.setOnClickListener(this@MainActivity)
            }
        }
    }

    private fun receiverSetup() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                play(intent.getParcelableExtra("song"))
            }
        }

        val intentFilter = IntentFilter(ACTION_SHOW)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun serviceSetup(){
        mBoundServiceIntent = Intent(this, MediaPlayerService::class.java)
        mBoundServiceIntent.action = MediaPlayerService.ACTION_CREATE

        startService(mBoundServiceIntent)
        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun play(song: Song?) {
        if (!mServiceBound) return
        try {
            if (song == null) {
                mService?.send(Message.obtain(null, MediaPlayerService.PLAY, 0, 0))
            } else {
                mService?.send(Message.obtain(null, MediaPlayerService.CHANGE, song.path))
                tvTitleNotify.text = song.title
                btnPlay.visibility = View.INVISIBLE
                btnPause.visibility = View.VISIBLE
            }
            notifyBar.visibility = View.VISIBLE
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPlay -> {
                play(null)
                btnPlay.visibility = View.INVISIBLE
                btnPause.visibility = View.VISIBLE
            }
            R.id.btnPause -> {
                play(null)
                btnPlay.visibility = View.VISIBLE
                btnPause.visibility = View.INVISIBLE
            }
            R.id.btnShow -> showPlayFragment()
            R.id.btnMenu -> Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            R.id.btnMic -> Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            R.id.btnOptions -> {
                val popupMenu = PopupMenu(this, v)
                popupMenu.inflate(R.menu.option_menu)
                Toast.makeText(this, "Tes", Toast.LENGTH_LONG).show()
//                popupMenu.setOnMenuItemClickListener(this)
            }
            R.id.btnAddPlaylist -> Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return true
    }

    fun changeMenu(position: Int){
        when(position){
            0 -> {
                supportActionBar?.customView?.btnMic?.visibility = View.VISIBLE
                supportActionBar?.customView?.btnOptions?.visibility = View.GONE
                supportActionBar?.customView?.btnAddPlaylist?.visibility = View.GONE
            }
            1 -> {
                supportActionBar?.customView?.btnMic?.visibility = View.GONE
                supportActionBar?.customView?.btnOptions?.visibility = View.VISIBLE
                supportActionBar?.customView?.btnAddPlaylist?.visibility = View.GONE
            }
            2 -> {
                supportActionBar?.customView?.btnMic?.visibility = View.GONE
                supportActionBar?.customView?.btnOptions?.visibility = View.GONE
                supportActionBar?.customView?.btnAddPlaylist?.visibility = View.VISIBLE
            }
        }
    }
    private fun showPlayFragment(){
//        val playFragment = ItemListDialogFragment()
//        playFragment.show(supportFragmentManager, playFragment.tag)

        startActivity(Intent(this, PlayActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
        mBoundServiceIntent.action = MediaPlayerService.ACTION_DESTROY
        startService(mBoundServiceIntent)
        unregisterReceiver(broadcastReceiver)
    }
}
