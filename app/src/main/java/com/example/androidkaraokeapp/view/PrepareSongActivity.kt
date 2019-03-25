package com.example.androidkaraokeapp.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.view.Fragment.UserFragment

class PrepareSongActivity : AppCompatActivity() {

    private lateinit var listRecordRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare_song)

        configureUI()
    }


    private fun configureUI () {
        supportFragmentManager.beginTransaction().replace(R.id.record_fragment_container, UserFragment()).commit()
    }
}
