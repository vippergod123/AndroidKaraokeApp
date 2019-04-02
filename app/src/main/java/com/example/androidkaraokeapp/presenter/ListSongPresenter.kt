package com.example.androidkaraokeapp.presenter

import android.util.Log
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.FirestoreUlti
import com.google.firebase.firestore.FirebaseFirestore


@Suppress("NAME_SHADOWING")
class ListSongPresenter : BasePresenter<ListSongContract.View>(), ListSongContract.Presenter {

    val db = FirebaseFirestore.getInstance()

    private val songNameCollection = FirestoreUlti.Collection_SONG
    private val favoriteSongNameCollection = FirestoreUlti.Collection_Favorite_Song



    override fun fetchListSongFromFirestore(listSong: MutableList<SongModel>){

        db.collection(songNameCollection).get().addOnSuccessListener { result ->
                                                    for (document in result) {
                                                        val song = document.toObject(SongModel::class.java )
                                                        listSong.add(song)
                                                    }
                                                    getView()?.showListSong()
                                                }
                                                .addOnFailureListener { exception ->
                                                    Log.w("Firestore ex", "Error getting documents.", exception)
                                                }

    }

    override fun fetchFavoriteSongFromFirestore(listSong: MutableList<SongModel>) {
        db.collection(favoriteSongNameCollection).get().addOnSuccessListener { result ->
                                                            for (document in result) {
                                                                val song = document.toObject(SongModel::class.java )
                                                                listSong.add(song)
                                                            }
                                                            getView()?.showListSong()
                                                        }
                                                        .addOnFailureListener { exception ->
                                                            Log.w("Firestore ex", "Error getting documents.", exception)
                                                        }
    }

    override fun addToFavorite(song: SongModel) {
        Log.d("add song", "add song")
    }

    override fun downloadSongToMemory(song: SongModel) {
        Log.d("download song", "download song")
    }



}