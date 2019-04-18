package com.example.androidkaraokeapp.presenter

import android.content.Context
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.model.SongModel

interface ListRecordContract {
    interface Presenter {
        fun fetchRecordFromFirestore(listRecord: MutableList<RecordModel>)
        fun findRecordBySong(listRecord: MutableList<RecordModel>, song:SongModel)
        fun deleteRecordFirestore(record: RecordModel, context: Context)
    }

    interface View {
        fun showListRecord()

        fun updateListRecord(removeRecord:RecordModel)
    }
}