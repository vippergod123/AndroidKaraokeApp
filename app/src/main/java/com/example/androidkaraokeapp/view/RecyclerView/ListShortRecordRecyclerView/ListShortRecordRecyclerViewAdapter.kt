package com.example.androidkaraokeapp.view.RecyclerView.ListShortRecordRecyclerView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.RecordModel

class ListShortRecordRecyclerViewAdapter : RecyclerView.Adapter<ListShortRecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ListShortRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.short_record_view_holder,parent,false)
        return ListShortRecordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(viewHolder: ListShortRecordViewHolder, position: Int) {
        val record = RecordModel("Chạm khẽ tim anh 1 chút hoy","asd","asd","http://2.bp.blogspot.com/-A_krgQz6ho4/UzgtCHoWo5I/AAAAAAAAXPY/a-iPk7UaWP8/s1600/background-don-gian-cho-windows+(3).jpg",10000,10000)

        viewHolder.bind(record)
    }

}
