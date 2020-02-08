package com.muklas.rungon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muklas.rungon.R
import com.muklas.rungon.model.Song
import kotlinx.android.synthetic.main.item_type1.view.*

class SongAdapter : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    private val listSongs = ArrayList<Song>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: ArrayList<Song>) {
        listSongs.clear()
        listSongs.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type1, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int = listSongs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(listSongs[position])
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(song: Song) {
            with(itemView) {
                tvTitle.text = song.title
                tvSubTitle.text = song.artist

                //check the image (album art)
                if(!song.image.isNullOrEmpty()){
                    Glide.with(context).load(song.image).into(ivAlbum)
                }

                //handle if this clicked
                setOnClickListener {
                    onItemClickCallback?.onItemClicked(song)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Song)
    }

}