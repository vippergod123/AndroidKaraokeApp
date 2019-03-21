package com.example.androidkaraokeapp.view.Fragment.ListSongRecyclerView

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.androidkaraokeapp.R


class ListSongViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    val songNameTextView = itemView?.findViewById<TextView>(R.id.song_name_text_view) as TextView
    fun bind (songName:String) {
        songNameTextView.text = songName
    }
}