package com.example.androidkaraokeapp.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.presenter.ListSongContract
import com.example.androidkaraokeapp.presenter.ListSongPresenter
import com.example.androidkaraokeapp.view.SearchActivity
import com.example.androidkaraokeapp.view.recyclerView.ListSongRecyclerView.ListSongRecyclerViewAdapter


class ListSongFragment : Fragment(), ListSongContract.View {

    private var listSongPresenter = ListSongPresenter()
    private lateinit var listSongRecyclerView: RecyclerView
    private lateinit var searchTextView : TextView
    private var listSong: MutableList<SongModel> = mutableListOf()
    private lateinit var listSongAdapter :ListSongRecyclerViewAdapter

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile

        private var instance: ListSongFragment? = null
        const val INTENT_LIST_SONG = "list_song"

        const val LIST_DETAIL_REQUEST_CODE = 123

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ListSongFragment().also { instance = it }
        }
    }

    //region Override system
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_song_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.let {
            configureUI(it)

        }
    }
    //endregion

    //region Override system
    override fun showListSong() {
        this.listSongRecyclerView.adapter = ListSongRecyclerViewAdapter(listSong)
    }
    //endregion

    private fun configureUI(view:View) {
        searchTextView = view.findViewById(R.id.search_text_view)
        listSongRecyclerView = view.findViewById(R.id.list_song_recycler_view)

        listSongRecyclerView.layoutManager = LinearLayoutManager(activity)
        listSongRecyclerView.adapter = ListSongRecyclerViewAdapter(listSong)
        listSongAdapter = listSongRecyclerView.adapter as ListSongRecyclerViewAdapter

        listSongPresenter.fetchListSongFromFirestore(listSong)

        searchTextView.setOnClickListener {
            val intent = Intent(it.context, SearchActivity::class.java)
// To pass any data to next activity
//            intent.putExtra(INTENT_LIST_SONG, listSong)
// start your next activity
            startActivity(intent)
        }
        setupPresenter()
    }
    private fun setupPresenter() {
        listSongPresenter.setView(this)
    }


}
