package com.example.androidkaraokeapp.ulti

import android.R.string.cancel
import android.content.Context
import android.os.Handler
import android.support.v4.os.HandlerCompat.postDelayed
import android.widget.Toast



class Handle_UI {
    fun toastWithDuration(text:String, duration:Long, context: Context) {
        val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.show()

        val handler = Handler()
        handler.postDelayed({ toast.cancel() }, duration * 1000)
    }
}