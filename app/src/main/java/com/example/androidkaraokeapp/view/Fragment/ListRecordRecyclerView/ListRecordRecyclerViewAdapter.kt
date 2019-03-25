package com.example.androidkaraokeapp.view.Fragment.ListRecordRecyclerView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.androidkaraokeapp.R

class ListRecordRecyclerViewAdapter : RecyclerView.Adapter<ListRecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ListRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_record_view_holder,parent,false)
        return ListRecordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(viewHolder: ListRecordViewHolder, position: Int) {
        viewHolder.bind("Asd")
    }

}
