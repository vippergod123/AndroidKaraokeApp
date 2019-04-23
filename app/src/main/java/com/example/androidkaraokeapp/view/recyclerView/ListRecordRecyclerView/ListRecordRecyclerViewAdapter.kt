package com.example.androidkaraokeapp.view.recyclerView.ListRecordRecyclerView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.presenter.ListRecordContract
import com.example.androidkaraokeapp.presenter.ListRecordPresenter
import java.io.File

class ListRecordRecyclerViewAdapter(private var listRecord: MutableList<RecordModel>, private var listRecordPresenter : ListRecordPresenter) : RecyclerView.Adapter<ListRecordViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ListRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_view_holder,parent,false)
        return ListRecordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listRecord.size
    }

    override fun onBindViewHolder(viewHolder: ListRecordViewHolder, position: Int) {

        val record = listRecord[position]
        viewHolder.bind(record, listRecordPresenter)
    }

    fun updateListRecord(listRecordFilterd: MutableList<RecordModel>) {
        listRecord = listRecordFilterd
        notifyDataSetChanged()
    }

}
