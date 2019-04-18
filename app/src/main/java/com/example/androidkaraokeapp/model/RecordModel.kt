package com.example.androidkaraokeapp.model

import java.io.Serializable

data class RecordModel (
    override var id:Int = -1, override var name:String = "",
    override var sub_url:String = "",
    override var thumbnail_url:String = "", override var alias:String = "",
    var user:String = "", var record_url:String = "", var create_time: Long = -1
):Serializable, SongModel(id, name, sub_url, thumbnail_url, alias)

//data class SongModel (var id:Int = -1,var name:String = "",
//                      var singer:String = "", var sub_url:String = "",
//                      var thumbnail_url:String = "", var alias:String = "",
//                      var mp3_url:String = ""): Serializable
