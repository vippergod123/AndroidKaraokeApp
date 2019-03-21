package com.example.androidkaraokeapp.view.Fragment.ListRecordRecyclerView

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.androidkaraokeapp.R

class ListRecordViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    private val recordSongNameTextView = itemView?.findViewById(R.id.record_song_name_text_view) as TextView
    fun bind (songName:String) {
        recordSongNameTextView.text = songName
    }
}