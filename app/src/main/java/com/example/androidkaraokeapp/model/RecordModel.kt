package com.example.androidkaraokeapp.model

data class RecordModel(var id:Int = -1,var name:String = "",
                       var user:String = "", var record_url:String = "",
                       var thumbnail_url:String = "", var duration: Int = -1,
                       var create_time: Long = -1)