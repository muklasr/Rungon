package com.muklas.rungon.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val album: String,
    val image: String,
    val path: String,
    val duration: Int
    ) : Parcelable