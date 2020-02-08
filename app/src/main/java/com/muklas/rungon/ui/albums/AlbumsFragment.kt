package com.muklas.rungon.ui.albums

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.muklas.rungon.R
import com.muklas.rungon.adapter.AlbumAdapter
import com.muklas.rungon.model.Song
import com.muklas.rungon.ui.songs.SongsViewModel
import kotlinx.android.synthetic.main.albums_fragment.*
import kotlinx.android.synthetic.main.albums_fragment.view.*

class AlbumsFragment : Fragment() {

    private lateinit var albumViewModel: AlbumsViewModel
    private var adapter = AlbumAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.albums_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData() {
        rvAlbums.layoutManager = LinearLayoutManager(context)
        adapter.setOnItemClickCallback(object : AlbumAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Song) {

            }
        })
        rvAlbums.adapter = adapter
        adapter.notifyDataSetChanged()

        albumViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(AlbumsViewModel::class.java)
        albumViewModel.setFiles(context as Context, SongsViewModel.TYPE_ALBUM)

        albumViewModel.getFiles().observe(this, Observer { songs ->
            if (!songs.isNullOrEmpty()) {
                adapter.setData(songs)
            }
        })
    }
}