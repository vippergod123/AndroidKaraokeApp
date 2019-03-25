package com.example.androidkaraokeapp.presenter

import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.FirestoreUlti
import com.example.androidkaraokeapp.view.Fragment.ListSongRecyclerView.ListSongRecyclerViewAdapter
import com.google.firebase.firestore.FirebaseFirestore


class ListSongPresenter : BasePresenter<ListSongContract.View>(), ListSongContract.Presenter {

    val db = FirebaseFirestore.getInstance()

    override fun fetchListSongFromFirestore(listSong: MutableList<SongModel>){
        db.collection("Song")
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