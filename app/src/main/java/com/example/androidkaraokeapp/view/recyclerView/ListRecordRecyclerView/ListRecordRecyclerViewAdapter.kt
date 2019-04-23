package com.example.androidkaraokeapp.view.recyclerView.ListRecordRecyclerView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.presenter.ListRecordPresenter
import com.example.androidkaraokeapp.ulti.ItemTouchHelperListener
import com.example.androidkaraokeapp.view.recyclerView.ListSongRecyclerView.ListSongViewHolder
import java.util.*

class ListRecordRecyclerViewAdapter(private var listRecord: MutableList<RecordModel>, private var listRecordPresenter : ListRecordPresenter) : RecyclerView.Adapter<ListRecordViewHolder>() ,
    ItemTouchHelperListener {

    private var lastPosition  = -1

    //region override
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
        setAnimation(viewHolder.itemView,position)
    }

    override fun onViewDetachedFromWindow(holder: ListRecordViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.clearAnimation()
    }
    //endregion

    //region method override ItemTouch Helper
    override fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean {

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition)
                Collections.swap(listRecord, i, i+1)
        }
        else {
            for ( i in fromPosition downTo toPosition + 1)
                Collections.swap(listRecord, i, i - 1)
        }
        notifyItemMoved(fromPosition,toPosition)
        return true
    }

    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val deleteRecord = listRecord[position]
        listRecordPresenter.deleteRecordFirestore(deleteRecord,viewHolder.itemView.context)
    }
    //endregion

    //region private method animation

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
    //endregion

    //region public method
    fun updateListRecord(listRecordFilterd: MutableList<RecordModel>) {
        listRecord = listRecordFilterd
        notifyDataSetChanged()
    }
    //endregion

}
