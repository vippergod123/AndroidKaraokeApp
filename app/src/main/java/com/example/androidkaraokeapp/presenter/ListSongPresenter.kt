package com.example.androidkaraokeapp.presenter

import android.util.Log
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.FirestoreUlti
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.lang.Exception


@Suppress("NAME_SHADOWING")
class ListSongPresenter : BasePresenter<ListSongContract.View>(), ListSongContract.Presenter {

    val db = FirebaseFirestore.getInstance()

    private val songNameCollection = FirestoreUlti.Collection_SONG
    private val favoriteSongNameCollection = FirestoreUlti.Collection_Favorite_Song



    override fun fetchListSongFromFirestore(listSong: MutableList<SongModel>, page: Int){
        val count = page.toLong() * 7
        db.collection(songNameCollection).orderBy("name", Query.Direction.ASCENDING).limit(count).get()
        .addOnSuccessListener { result ->
            val temp: MutableList<SongModel> = mutableListOf()
            for (document in result) {
                val song = document.toObject(SongModel::class.java )
                try {
                song.isLiked = document.data["isLiked"] as Boolean
                }
                catch (ex:Exception) { }
                temp.add(song)
            }
            listSong.clear()
            listSong.addAll(temp)
            getView()?.showListSong()
        }
        .addOnFailureListener { exception ->
            Log.w("Firestore ex", "Error getting documents.", exception)
        }
    }

    override fun fetchFavoriteSongFromFirestore(listSong: MutableList<SongModel>) {
        db.collection(songNameCollection).whereEqualTo("isLiked", true).get().addOnSuccessListener { result ->
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