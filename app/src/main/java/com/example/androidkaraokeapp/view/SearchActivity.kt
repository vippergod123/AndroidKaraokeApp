package com.example.androidkaraokeapp.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import com.example.androidkaraokeapp.R

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var songNameFilterButton: Button
    private lateinit var songSingerFilterButton: Button


    private var filterType: String = "name"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        configureUI()
        setViewListener()
    }


    private fun configureUI(){
        //find by ID
        searchRecyclerView = findViewById(R.id.search_recycler_view)
        searchView = findViewById(R.id.search_view)
        songSingerFilterButton = findViewById(R.id.song_singer_filter_button)
        songNameFilterButton = findViewById(R.id.song_name_filter_button)

        // Configure
        searchView.isFocusable = true
        searchView.setIconifiedByDefault(false)
        searchView.requestFocus()

    }


    private fun setViewListener(){
        songNameFilterButton.setOnClickListener {
            filterType = "name"
        }
        songSingerFilterButton.setOnClickListener {
            filterType = "singer"
        }
    }
}
