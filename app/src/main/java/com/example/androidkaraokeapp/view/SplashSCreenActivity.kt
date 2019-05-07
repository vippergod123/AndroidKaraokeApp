package com.example.androidkaraokeapp.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.androidkaraokeapp.R
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.lang.Exception

class SplashSCreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        splash_image_view.setBackgroundResource( R.drawable.karaoke_background)
        val background = object : Thread() {
            override fun run() {
                super.run()
                try {

                    sleep(0*1000)
                    val intent = Intent(baseContext,MainActivity::class.java)
                    startActivity(intent)
                }
                catch (ex:Exception) {
                    ex.printStackTrace()
                }
            }
        }
        background.start()
    }
}
