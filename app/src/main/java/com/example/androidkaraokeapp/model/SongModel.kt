package com.example.androidkaraokeapp.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

//
data class SongModel (var id:Int = -1,var name:String = "",
                      var singer:String = "", var sub_url:String = "",
                      var thumbnail_url:String = "", var alias:String = "",
                      var mp3_url:String = ""): Serializable

