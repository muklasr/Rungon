package com.muklas.rungon.service

interface MediaPlayerCallback {
    fun onChange(song: String)
    fun onStart()
    fun onStop()
}