package com.example.androidkaraokeapp.ulti

import android.content.Context
import android.os.Handler
import android.widget.Toast



class Handle_UI {
    fun toastWithDuration(text:String, secondDuration:Long, context: Context) {
        val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.show()

        val handler = Handler()
        handler.postDelayed({ toast.cancel() }, secondDuration * 1000)
    }
}