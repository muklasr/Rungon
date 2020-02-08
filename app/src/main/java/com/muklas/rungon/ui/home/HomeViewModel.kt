package com.muklas.rungon.ui.home

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muklas.rungon.model.Song


class HomeViewModel : ViewModel() {

    val list = MutableLiveData<ArrayList<Song>>()
    companion object{
        const val TYPE_SONG = 0
        const val TYPE_ALBUM = 1
        const val TYPE_ARTIST = 2
    }

    internal fun setFiles(context: Context, type: Int) {
        val listSongs = ArrayList<Song>()
        var group = ""
        //filter
        when(type){
            TYPE_SONG -> group = ""
            TYPE_ALBUM -> group = ") GROUP BY ("+ MediaStore.Audio.Media.ALBUM_ID
            TYPE_ARTIST -> group = ") GROUP BY ("+MediaStore.Audio.Media.ARTIST_ID
        }

        //get external storage uri
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        //filter the media
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"+group
        //sorting the result by title
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        //execute query
        val cursor = context.contentResolver.query(
            uri,
            null,
            selection,
            null,
            sortOrder
        )

        //check the cursor, is it null or not
        if (cursor != null && cursor.moveToFirst()) {
            //get the index for each column
            val id = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val path = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)

            //loop get all item
            do {
                //get song's data by index
                val audioId = cursor.getInt(id)
                val audioTitle = cursor.getString(title)
                val audioArtist = cursor.getString(artist)
                val audioAlbum = cursor.getString(album)
                val audioDuration = cursor.getInt(duration)
                val audioAlbumId = cursor.getString(albumId)
                val audioPath = cursor.getString(path)

                //get the album art (path)
                val albumCursor = context.contentResolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    null,
                    MediaStore.Audio.Albums._ID + "=?",
                    arrayOf(audioAlbumId),
                    null
                )

                //initialize the variable
                var image = ""
                //using try and catch to handle error if the album art is null/empty
                try {
                    albumCursor?.moveToFirst()
                    albumCursor?.let {
                        image =
                            albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                //bundle the data into song object
                val song = Song(
                    audioId, audioTitle, audioArtist, audioAlbum, image, audioPath, audioDuration
                )
                //add the song object into listSongs
                listSongs.add(song)
            } while (cursor.moveToNext())
            //post listSongs into list after the loop end
            list.postValue(listSongs)
        }

    }

    internal fun getFiles(): LiveData<ArrayList<Song>> {
        return list
    }
}