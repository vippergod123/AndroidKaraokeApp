package com.example.androidkaraokeapp.presenter

import android.util.Log
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.FirestoreUlti
import com.google.firebase.firestore.FirebaseFirestore


@Suppress("NAME_SHADOWING")
class ListSongPresenter : BasePresenter<ListSongContract.View>(), ListSongContract.Presenter {


    val db = FirebaseFirestore.getInstance()
    private val songCollectionString = FirestoreUlti.Collection_SONG
    private val favoriteSongCollectionString = FirestoreUlti.Collection_Favorite_Song

    override fun fetchListSongFromFirestore(listSong: MutableList<SongModel>){

        db.collection(songCollectionString)
            .get()
            .addOnSuccessListener { result ->
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
        db.collection(favoriteSongCollectionString)
            .get()
            .addOnSuccessListener { result ->
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



}