package com.example.androidkaraokeapp.view.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.view.Fragment.ListRecordRecyclerView.ListRecordRecyclerViewAdapter

class UserFragment: Fragment() {
    private lateinit var listRecordRecyclerView: RecyclerView
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
        return inflater.inflate(R.layout.user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(ListSongViewModel::class.java)
        view?.let{
            listRecordRecyclerView = it.findViewById(R.id.list_record_recycler_view)
            listRecordRecyclerView.layoutManager = LinearLayoutManager(activity)
            listRecordRecyclerView.adapter =  ListRecordRecyclerViewAdapter()

        }
    }
}