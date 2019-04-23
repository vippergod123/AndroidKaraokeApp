package com.example.androidkaraokeapp.view.recyclerView.ListSongRecyclerView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.FirestoreUlti
import com.example.androidkaraokeapp.ulti.ItemTouchHelperListener
import com.example.androidkaraokeapp.view.dialog.ListSongDialogFragment
import kotlinx.android.synthetic.main.list_song_view_holder.view.*
import java.util.*



class ListSongRecyclerViewAdapter(private var listSong:MutableList<SongModel>) : RecyclerView.Adapter<ListSongViewHolder>(), ItemTouchHelperListener {
    private var lastPosition  = -1
    var isVisibleFavoriteSongImageButton = true


    //#region override recyclerView
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ListSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_song_view_holder,parent,false)
        return ListSongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSong.size
    }

    override fun onBindViewHolder(viewHolder: ListSongViewHolder, position: Int) {
        val song = listSong[position]
        viewHolder.bind(song)
        if (isVisibleFavoriteSongImageButton )
            viewHolder.setupFavoriteImageButton(song)
        else
            viewHolder.itemView.favorite_song_image_button.visibility = View.INVISIBLE

        setAnimation(viewHolder.itemView,position)
    }

    override fun onViewDetachedFromWindow(holder: ListSongViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.clearAnimation()
    }
    //#endregion

    //#region item touch helper

    override fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition)
                Collections.swap(listSong, i, i+1)
        }
        else {
            for ( i in fromPosition downTo toPosition + 1)
                Collections.swap(listSong, i, i - 1)
        }
        notifyItemMoved(fromPosition,toPosition)
        return true
    }

    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val dismissSong = listSong[position]
        val favSongCollection = FirestoreUlti.getInstance().db.collection(FirestoreUlti.Collection_Favorite_Song)
        listSong.removeAt(position)
        notifyItemRemoved(position)
        favSongCollection.document(dismissSong.id.toString()).delete()
        .addOnSuccessListener {
//            listSong.removeAt(position)

        }
        .addOnFailureListener {  }

    }

    //#endregion

    //#region private Method animation

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    //endregion

    //#region public Method
    fun updateListSong(listSongFiltered: MutableList<SongModel>) {
        listSong = listSongFiltered
        notifyDataSetChanged()
    }
    //#endregion

}

