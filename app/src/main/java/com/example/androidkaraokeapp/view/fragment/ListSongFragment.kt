package com.example.androidkaraokeapp.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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

    private var page = 1
    private var loading = true

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
            listSongPresenter.fetchListSongFromFirestore(listSong, page)
        }
    }
    //endregion

    //region Override system
    override fun showListSong() {
        listSongRecyclerView.adapter!!.notifyDataSetChanged()
    }
    //endregion

    private fun configureUI(view:View) {
        searchTextView = view.findViewById(R.id.search_text_view)
        listSongRecyclerView = view.findViewById(R.id.list_song_recycler_view)

        listSongRecyclerView.layoutManager = LinearLayoutManager(activity)
        listSongRecyclerView.adapter = ListSongRecyclerViewAdapter(listSong)
        listSongAdapter = listSongRecyclerView.adapter as ListSongRecyclerViewAdapter

        setupListener()
        setupPresenter()
    }

    private fun setupListener() {
        searchTextView.setOnClickListener {
            val intent = Intent(it.context, SearchActivity::class.java)
            startActivity(intent)
        }


        listSongRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val mLayoutManager = listSongRecyclerView.layoutManager as LinearLayoutManager
                if (dy > 0) {
                    val visibleItemCount = mLayoutManager.childCount
                    val totalItemCount = mLayoutManager.itemCount
                    val pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()

                    if (loading && page <= 3) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false
                            page++
                            listSongPresenter.fetchListSongFromFirestore(listSong, page)
                            Handler().postDelayed({
                                loading = true
                            },200)
                        }
                    }
                }
            }
        })
    }
    private fun setupPresenter() {
        listSongPresenter.setView(this)
    }


}
