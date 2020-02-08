package com.muklas.rungon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muklas.rungon.R
import com.muklas.rungon.model.Song
import kotlinx.android.synthetic.main.item_artist.view.*

class TopArtistAdapter : RecyclerView.Adapter<TopArtistAdapter.ArtistViewHolder>() {
    private val list = ArrayList<Song>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(data: ArrayList<Song>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return ArtistViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(song: Song) {
            with(itemView) {
                tvArtist.text = song.artist
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Song)
    }

}