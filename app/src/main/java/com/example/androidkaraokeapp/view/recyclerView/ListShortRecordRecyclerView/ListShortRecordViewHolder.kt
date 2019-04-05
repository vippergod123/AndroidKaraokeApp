package com.example.androidkaraokeapp.view.recyclerView.ListShortRecordRecyclerView

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.ulti.HandleDateTime


@SuppressLint("SetTextI18n")
class ListShortRecordViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    private val shortRecordDurationTimeTextView = itemView?.findViewById(R.id.short_record_duration_time_text_view) as TextView
    private val shortRecordUserNameTextView = itemView?.findViewById(R.id.short_recrod_user_name_text_view) as TextView



    fun bind (record: RecordModel) {

//        shortRecordUserNameTextView.text = record.user
//        shortRecordDurationTimeTextView.text = "Thời gian: ${record.duration} - ${record.create_time}"


        shortRecordUserNameTextView.text = record.user

        try {

            val mmr = MediaMetadataRetriever()

            mmr.setDataSource(record.record_url)
            val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

            val durationTimeString = HandleDateTime.miliSecondToTime(duration.toLong())
            val dateCreateString = HandleDateTime.miliSecondToDateFormat(record.create_time, "dd/MM/yyyy hh:mm")

            shortRecordDurationTimeTextView.text = "Thời gian: $durationTimeString - $dateCreateString"
        } catch (ex: RuntimeException) {
            // something went wrong with the file, ignore it and continue
        }
    }
}