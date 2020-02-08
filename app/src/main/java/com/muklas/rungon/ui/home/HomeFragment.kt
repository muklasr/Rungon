package com.muklas.rungon.ui.home

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
import com.muklas.rungon.adapter.TopAlbumAdapter
import com.muklas.rungon.adapter.TopArtistAdapter
import com.muklas.rungon.model.Song
import com.muklas.rungon.ui.songs.SongsViewModel
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    private var artistAdapter = TopArtistAdapter()
    private var albumAdapter = TopAlbumAdapter()
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTopArtists()
        loadTopAlbums()
    }

    private fun loadTopArtists() {
        rvTopArtists.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvTopArtists.adapter = artistAdapter
        artistAdapter.notifyDataSetChanged()
        artistAdapter.setOnItemClickCallback(object : TopArtistAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Song) {

            }

        })

        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)
        homeViewModel.setFiles(context as Context, SongsViewModel.TYPE_ARTIST)
        homeViewModel.getFiles().observe(this, Observer {
            if(!it.isNullOrEmpty()){
                artistAdapter.setData(it)
            }
        })
    }

    private fun loadTopAlbums() {
        rvTopAlbums.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvTopAlbums.adapter = albumAdapter
        albumAdapter.notifyDataSetChanged()
        albumAdapter.setOnItemClickCallback(object : TopAlbumAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Song) {

            }

        })

        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)
        homeViewModel.setFiles(context as Context, SongsViewModel.TYPE_ALBUM)
        homeViewModel.getFiles().observe(this, Observer {
            if(!it.isNullOrEmpty()){
                albumAdapter.setData(it)
            }
        })
    }
}