package com.example.androidkaraokeapp.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.presenter.ListSongContract
import com.example.androidkaraokeapp.presenter.ListSongPresenter
import com.example.androidkaraokeapp.ulti.HandleString
import com.example.androidkaraokeapp.ulti.ItemTouchHelperCallback
import com.example.androidkaraokeapp.view.recyclerView.ListSongRecyclerView.ListSongRecyclerViewAdapter



@Suppress("NAME_SHADOWING")
class FavoriteSongFragment : Fragment(), ListSongContract.View {

    private var listSongPresenter = ListSongPresenter()
    private lateinit var favoriteSongRecyclerView: RecyclerView
    private var listSong: MutableList<SongModel> = mutableListOf()
    private lateinit var favoriteSongAdapter :ListSongRecyclerViewAdapter
    private lateinit var searchFavoriteSongEditText : EditText
    private var searchString = ""

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: FavoriteSongFragment? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: FavoriteSongFragment().also { instance = it }
        }
    }

    //#region override system
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_song_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.let{
            configureUI(it)
            setupPresenter()
            listSongPresenter.fetchFavoriteSongFromFirestore(listSong)
        }
    }
    //#endregion


    //#region override
    override fun showListSong() {
        favoriteSongAdapter.notifyDataSetChanged()
    }
    //#endregion

    //#region  private Method
    private fun setupPresenter() {
        listSongPresenter.setView(this)
    }

    private fun configureUI (it:View) {
        favoriteSongRecyclerView = it.findViewById(R.id.favorite_song_recycler_view)
        searchFavoriteSongEditText = it.findViewById(R.id.search_favorite_song_edit_view)
        
        favoriteSongRecyclerView.layoutManager = LinearLayoutManager(activity)
        favoriteSongRecyclerView.adapter = ListSongRecyclerViewAdapter(listSong)
        favoriteSongAdapter = favoriteSongRecyclerView.adapter as ListSongRecyclerViewAdapter
        favoriteSongAdapter.isVisibleFavoriteSongImageButton = false

        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(favoriteSongAdapter, context))
        itemTouchHelper.attachToRecyclerView(favoriteSongRecyclerView)


        searchFavoriteSongEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterListSong(s.toString())
            }

        })

    }


    private fun filterListSong(input:String) {
        searchString = input
        val filteredMap: List<SongModel> = listSong.filter{
            val nameSong = HandleString().removeVietnameseUnicodeSymbol(it.name)
            val nameSearch = HandleString().removeVietnameseUnicodeSymbol(searchString)
            nameSong.contains(nameSearch)
        }

        favoriteSongAdapter.updateListSong(filteredMap as MutableList<SongModel>)
    }
    //#endregion



}
