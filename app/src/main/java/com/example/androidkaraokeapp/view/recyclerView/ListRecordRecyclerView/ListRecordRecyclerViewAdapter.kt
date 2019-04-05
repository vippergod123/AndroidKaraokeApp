package com.example.androidkaraokeapp.view.recyclerView.ListRecordRecyclerView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.RecordModel
import java.io.File

class ListRecordRecyclerViewAdapter(mContext:Context) : RecyclerView.Adapter<ListRecordViewHolder>() {

    private var context: Context = mContext
    private var listRecord: MutableList<RecordModel> = mutableListOf()

    init {
        val recordFolder = context.filesDir.path + "/record"
        File(recordFolder).listFiles().forEach {
            println(it.name)
            val pathNameSplit = it.name.split(".")
            val recordDetail = pathNameSplit[0].split("_")
            println(recordDetail[recordDetail.lastIndex])
            val record = RecordModel()
            record.user =  recordDetail[0]
            record.id = recordDetail[1].toInt()
            record.name = recordDetail[2]
            record.create_time = recordDetail[3].toLong()
            record.record_url = it.path
            listRecord.add(record)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ListRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_view_holder,parent,false)
        return ListRecordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listRecord.size
    }

    override fun onBindViewHolder(viewHolder: ListRecordViewHolder, position: Int) {

        val record = listRecord[position]
        viewHolder.bind(record)
    }

}
