package com.example.androidkaraokeapp.view.Fragment

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
import com.example.androidkaraokeapp.view.Fragment.ListSongRecyclerView.ListSongRecyclerViewAdapter
import com.example.androidkaraokeapp.view.MainActivity
import com.example.androidkaraokeapp.view.SearchActivity


class ListSongFragment : Fragment() {

    private lateinit var listSongRecyclerView: RecyclerView
    private lateinit var searchTextView : TextView
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: ListSongFragment? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ListSongFragment().also { instance = it }
        }
    }
//    private lateinit var viewModel: ListSongViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.list_song_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(ListSongViewModel::class.java)
        view?.let{
            listSongRecyclerView = it.findViewById(R.id.list_song_recycler_view)
            listSongRecyclerView.layoutManager = LinearLayoutManager(activity)
            listSongRecyclerView.adapter =  ListSongRecyclerViewAdapter()


            searchTextView = view!!.findViewById(R.id.search_text_view)

            searchTextView.setOnClickListener {
                val intent = Intent(view!!.context, SearchActivity::class.java)
// To pass any data to next activity
//            intent.putExtra("keyIdentifier", value)
// start your next activity
                startActivity(intent)

            }
        }


    }

}
