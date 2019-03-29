package com.example.androidkaraokeapp.view.RecyclerView.ListRecordRecyclerView

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.RecordModel


class ListRecordViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    private val recordSongNameTextView = itemView?.findViewById(R.id.record_song_name_text_view) as TextView
    private val recordUserNameTextView = itemView?.findViewById(R.id.record_user_name_text_view) as TextView
    private val recordDurationTextView = itemView?.findViewById(R.id.record_duration_text_view) as TextView

    private val recordImageView = itemView?.findViewById(R.id.record_image_view) as ImageView

    @SuppressLint("SetTextI18n")
    fun bind (record:RecordModel) {
        recordSongNameTextView.text = record.name
        recordUserNameTextView.text = "User: ${record.user}"
        recordDurationTextView.text = record.duration.toString()


        Picasso.get().load(record.thumbnail_url)
                .into(recordImageView)
    }
}