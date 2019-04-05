package com.example.androidkaraokeapp.ulti

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


object HandleDateTime {
    fun timeToMiliSeconds(time:String):Float {
        val timeSplit = time.split(":")
        return timeSplit[0].toFloat() * 60 * 1000 + timeSplit[1].toFloat() * 1000
    }

    fun miliSecondToTime (mil: Long):String {
        val minutes = mil / 1000 / 60
        val seconds = mil / 1000 % 60


        var minString = minutes.toString()
        var secString = seconds.toString()



        if ( minutes < 10) {
            minString = "0$minutes"
        }

        if (seconds < 10) {
            secString = "0$seconds"
        }
        return "$minString:$secString"
    }

    @SuppressLint("SimpleDateFormat")
    fun miliSecondToDateFormat (mil:Long, dateFormat: String):String {
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mil

        return formatter.format(calendar.time)
    }

}
