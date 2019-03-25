package com.example.androidkaraokeapp.ulti

import android.annotation.SuppressLint
import android.util.Log
import com.example.androidkaraokeapp.model.SongModel
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreUlti {

    val db = FirebaseFirestore.getInstance()

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile

        private var instance: FirestoreUlti? = null

        const val INTENT_LIST_SONG = "list_song"
        const val LIST_DETAIL_REQUEST_CODE = 123

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: FirestoreUlti().also { instance = it }
        }
    }

    fun fetchListSongFromFirestore () {

    }
}