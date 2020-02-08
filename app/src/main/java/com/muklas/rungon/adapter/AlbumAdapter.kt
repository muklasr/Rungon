package com.muklas.rungon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muklas.rungon.R
import com.muklas.rungon.model.Song
import kotlinx.android.synthetic.main.item_album.view.*
import kotlinx.android.synthetic.main.item_album.view.ivAlbum
import kotlinx.android.synthetic.main.item_type1.view.*

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
    private val list = ArrayList<Song>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(data: ArrayList<Song>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type1, parent, false)
        return AlbumViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(song: Song) {
            with(itemView){
                tvTitle.text = song.album
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

    interface OnItemClickCallback{
        fun onItemClicked(data: Song)
    }
}