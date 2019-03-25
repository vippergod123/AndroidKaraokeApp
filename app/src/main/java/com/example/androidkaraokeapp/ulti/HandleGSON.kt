package com.example.androidkaraokeapp.ulti

import com.example.androidkaraokeapp.model.SongModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class HandleGSON {

    fun GSONToSongModel (json:String): SongModel {

        val gson = Gson()

        val song : SongModel = gson.fromJson(json, SongModel::class.java)

        return song
    }

    fun SongModelToGSon(song: SongModel): String? {
        val gson = GsonBuilder().setPrettyPrinting().create()
        println(gson.toJson(song))
        return gson.toJson(song)
    }
}