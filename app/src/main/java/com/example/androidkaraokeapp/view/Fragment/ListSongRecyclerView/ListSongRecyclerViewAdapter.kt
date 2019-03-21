package com.example.androidkaraokeapp.view.Fragment.ListSongRecyclerView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.androidkaraokeapp.R

class ListSongRecyclerViewAdapter() : RecyclerView.Adapter<ListSongViewHolder>() {
    override fun onBindViewHolder(viewHolder: ListSongViewHolder, position: Int) {
        viewHolder.bind("Asd")
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ListSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_song_view_holder,parent,false)
        return ListSongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

}

