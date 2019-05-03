package com.example.androidkaraokeapp.presenter

import android.support.v7.widget.RecyclerView
import com.example.androidkaraokeapp.model.SongModel


interface ListSongContract {
    interface Presenter {
        fun fetchListSongFromFirestore(listSong: MutableList<SongModel>, page:Int)
        fun fetchFavoriteSongFromFirestore(listSong: MutableList<SongModel>)

        fun addToFavorite ( song: SongModel)
        fun downloadSongToMemory (song: SongModel)
    }

    interface View {
        fun showListSong()
    }
}