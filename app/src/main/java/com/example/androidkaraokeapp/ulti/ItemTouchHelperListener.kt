package com.example.androidkaraokeapp.ulti

import android.support.v7.widget.RecyclerView

interface ItemTouchHelperListener {
    fun onItemMove ( recyclerView: RecyclerView, fromPosition:Int, toPosition:Int):Boolean
    fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int)
}