package com.example.androidkaraokeapp.ulti

object HandleDateTime {
    fun timeToMiliSeconds(time:String):Float {
        val timeSplit = time.split(":")
        return timeSplit[0].toFloat() * 60 * 1000 + timeSplit[1].toFloat() * 1000
    }

    fun miliSecondToTime (mil: Int):String {
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
}
