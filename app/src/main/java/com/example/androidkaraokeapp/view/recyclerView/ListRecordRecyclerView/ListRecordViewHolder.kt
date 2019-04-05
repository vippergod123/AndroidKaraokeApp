package com.example.androidkaraokeapp.view.recyclerView.ListRecordRecyclerView

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.RecordModel
import android.media.MediaMetadataRetriever
import com.example.androidkaraokeapp.ulti.HandleDateTime
import com.squareup.picasso.Picasso


class ListRecordViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    private val recordSongNameTextView = itemView?.findViewById(R.id.record_song_name_text_view) as TextView
    private val recordUserNameTextView = itemView?.findViewById(R.id.record_user_name_text_view) as TextView
    private val recordDurationTextView = itemView?.findViewById(R.id.record_duration_text_view) as TextView

    private val recordImageView = itemView?.findViewById(R.id.record_image_view) as ImageView

    @SuppressLint("SetTextI18n")
    fun bind (record:RecordModel) {
        recordSongNameTextView.text = record.name
        val createTime = HandleDateTime.miliSecondToDateFormat(record.create_time, "dd/MM/yyyy hh:mm")
        recordUserNameTextView.text = "User: ${record.user} - $createTime"

        try {
            val mmr = MediaMetadataRetriever()

            mmr.setDataSource(record.record_url)
            val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val timeString = HandleDateTime.miliSecondToTime(duration.toLong())
            recordDurationTextView.text = "Thời gian: $timeString"

        } catch (ex: RuntimeException) {
            // something went wrong with the file, ignore it and continue
        }



        Picasso.get().load("https://i.ytimg.com/vi/AkPGQyCKj7s/maxresdefault.jpg")
                .into(recordImageView)
    }
}