package com.muklas.rungon.ui.songs

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.muklas.rungon.R
import com.muklas.rungon.adapter.SongAdapter
import com.muklas.rungon.model.Song
import com.muklas.rungon.service.PlayService
import kotlinx.android.synthetic.main.songs_fragment.view.*

class SongsFragment : Fragment() {

    private lateinit var songViewModel: SongsViewModel
    private lateinit var root: View
    private var adapter = SongAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.songs_fragment, container, false)
        loadData()
        return root
    }

    private fun loadData() {
        root.rvSongs.layoutManager = LinearLayoutManager(root.context)
        root.rvSongs.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : SongAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Song) {
                val notifyService = Intent(root.context, PlayService::class.java)
                notifyService.putExtra("song", data)
                root.context.startService(notifyService)
            }
        })

        songViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SongsViewModel::class.java)
        songViewModel.setFiles(root.context, SongsViewModel.TYPE_SONG)

        songViewModel.getFiles().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                adapter.setData(it)
            }
        })
    }
}