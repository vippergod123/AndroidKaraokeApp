package com.example.androidkaraokeapp.model

import java.io.Serializable

//
 open class SongModel (
    open var id:Int = -1, open var name:String = "",
    open var singer:String = "", open var sub_url:String = "",
    open var thumbnail_url:String = "", open var alias:String = "",
    open var mp3_url:String = ""): Serializable

