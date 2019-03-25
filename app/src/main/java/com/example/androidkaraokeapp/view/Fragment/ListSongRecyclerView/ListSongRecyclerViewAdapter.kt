package com.example.androidkaraokeapp.view.Fragment.ListSongRecyclerView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel

class ListSongRecyclerViewAdapter(private var listSong:MutableList<SongModel>) : RecyclerView.Adapter<ListSongViewHolder>() {

    private var lastPosition  = -1

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ListSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_song_view_holder,parent,false)
        return ListSongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSong.size
    }

    override fun onBindViewHolder(viewHolder: ListSongViewHolder, position: Int) {
        viewHolder.bind(listSong[position])

        setAnimation(viewHolder.itemView,position)


    }

    override fun onViewDetachedFromWindow(holder: ListSongViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.clearAnimation()
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    fun updateListSong(listSongFiltered: MutableList<SongModel>) {
//        listSong = listSong.filter{ it.name.toLowerCase().contains(inputSearch.toLowerCase())}

        listSong = listSongFiltered
        notifyDataSetChanged()
    }
}

