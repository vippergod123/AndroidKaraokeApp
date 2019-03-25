package com.example.androidkaraokeapp.presenter

import android.support.v7.widget.RecyclerView
import com.example.androidkaraokeapp.model.SongModel


interface ListSongContract {
    interface Presenter {
         fun fetchListSongFromFirestore(listSong: MutableList<SongModel>)

    }

    interface View {
        fun showListSong()
    }
}