package com.example.androidkaraokeapp.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.presenter.ListSongContract
import com.example.androidkaraokeapp.presenter.ListSongPresenter
import com.example.androidkaraokeapp.ulti.HandleString
import com.example.androidkaraokeapp.view.recyclerView.ListSongRecyclerView.ListSongRecyclerViewAdapter
import java.util.regex.Pattern

class SearchActivity : AppCompatActivity(), ListSongContract.View {

    private lateinit var searchView: SearchView

    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: ListSongRecyclerViewAdapter
    private lateinit var songNameFilterButton: Button
    private lateinit var songSingerFilterButton: Button

    private var songFilterTypeString = "name"
    private var page = 100



    private var listSongPresenter = ListSongPresenter()
    private var listSong: MutableList<SongModel> = mutableListOf()

    private var searchString = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        configureUI()
        setViewListener()
        setupPresenter()

        listSongPresenter.fetchListSongFromFirestore(listSong,page)
    }

    override fun onResume() {
        super.onResume()
        recyclerViewAdapter.isVisibleFavoriteSongImageButton = false
    }

    override fun showListSong() {
        recyclerViewAdapter.notifyDataSetChanged()
    }


    private fun configureUI(){
        //find by ID
        searchRecyclerView = findViewById(R.id.search_recycler_view)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerView.adapter = ListSongRecyclerViewAdapter(listSong)

        searchView = findViewById(R.id.search_view)
        songSingerFilterButton = findViewById(R.id.song_singer_filter_button)
        songNameFilterButton = findViewById(R.id.song_name_filter_button)

        // Configure
        recyclerViewAdapter = searchRecyclerView.adapter as ListSongRecyclerViewAdapter
        recyclerViewAdapter.isVisibleFavoriteSongImageButton = false

        searchView.isFocusable = true
        searchView.setIconifiedByDefault(false)
        searchView.requestFocus()

        songNameFilterButton.isSelected = true
        songSingerFilterButton.isSelected = !songNameFilterButton.isSelected

    }


    private fun setViewListener(){
        songNameFilterButton.setOnClickListener {
            if (songSingerFilterButton.isSelected) {
                songNameFilterButton.isSelected = true
                songSingerFilterButton.isSelected = false
            }
            songFilterTypeString = "name"
            filterListSong(searchString)
        }
        songSingerFilterButton.setOnClickListener {
            if (songNameFilterButton.isSelected) {
                songSingerFilterButton.isSelected = true
                songNameFilterButton.isSelected = false
            }

            songFilterTypeString = "singer"
            filterListSong(searchString)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(input: String): Boolean {
                filterListSong(input)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("asda", "asda")
                return false
            }
        })
    }

    private fun setupPresenter() {
        listSongPresenter.setView(this)
    }

    private fun filterListSong(input:String) {
        searchString = input
        val numberID = searchString.toIntOrNull()


        songFilterTypeString = if (songNameFilterButton.isSelected) "name" else "singer"

        if (numberID != null)
            songFilterTypeString = "number_id"

        if (!input.contains(" ") && songFilterTypeString == "name" && input != "")
            songFilterTypeString = "name_shortcut"


        val filteredMap: List<SongModel> = when (songFilterTypeString) {
            "name" -> listSong.filter {
                val nameSong = HandleString().removeVietnameseUnicodeSymbol(it.name)
                val nameSearch = HandleString().removeVietnameseUnicodeSymbol(searchString)
                nameSong.contains(nameSearch)
            }
            "singer" -> listSong.filter {
                val singer = HandleString().removeVietnameseUnicodeSymbol(it.singer)
                val singerSearch = HandleString().removeVietnameseUnicodeSymbol(searchString)
                singer.contains(singerSearch)
            }
            "number_id" -> listSong.filter {
                val id = it.id.toString()
                id.contains(searchString)
            }
            "name_shortcut" -> listSong.filter {
                val splitName = it.name.split(" ")
                var firstCharInName = ""
                splitName.forEach { word ->
                    firstCharInName+= word[0].toLowerCase()
                }
                firstCharInName = HandleString().removeVietnameseUnicodeSymbol(firstCharInName)
                val inputSearch = HandleString().removeVietnameseUnicodeSymbol(input)
                firstCharInName.contains(inputSearch)
            }
            else -> listSong.filter {
                val nameSong = HandleString().removeVietnameseUnicodeSymbol(it.name)
                val nameSearch = HandleString().removeVietnameseUnicodeSymbol(searchString)
                nameSong.contains(nameSearch)
            }
        }

        recyclerViewAdapter.updateListSong(filteredMap as MutableList<SongModel>)
    }
}
