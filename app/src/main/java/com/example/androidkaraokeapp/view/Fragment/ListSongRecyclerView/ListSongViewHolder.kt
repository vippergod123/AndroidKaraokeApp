package com.example.androidkaraokeapp.view.Fragment.ListSongRecyclerView

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.HandleMemoryStorage
import com.example.androidkaraokeapp.view.PrepareSongActivity
import com.squareup.picasso.Picasso





class ListSongViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    private val songNameTextView = itemView?.findViewById(R.id.song_name_text_view) as TextView
    private val songSingerTextView = itemView?.findViewById(R.id.song_singer_text_view) as TextView
    private val favoriteSongImageButton = itemView?.findViewById(R.id.favorite_song_image_button) as ImageButton
    private val songThumbnailImageView = itemView?.findViewById<ImageView>(R.id.song_thumbnail_image_view)


    fun bind (song: SongModel) {
        songNameTextView.text = song.name
        songSingerTextView.text = song.singer

        Picasso.get().load(song.thumbnail_url).into(songThumbnailImageView)
        itemView.setOnClickListener {
            val intent = Intent(it.context, PrepareSongActivity::class.java)
// To pass any data to next activity
//            intent.putExtra("keyIdentifier", value)
// start your next activity
            it.context.startActivity(intent)
        }

        favoriteSongImageButton.setOnClickListener { it ->
//            val toastDuration = 1.toLong()
//            val file = it.context.getFileStreamPath(song.name)
//            if (file.exists()) {
//                Handle_UI().toastWithDuration("Bài hát này đã được lưu",toastDuration,it.context.applicationContext)
//            }
//            else {
//                val filename = song.name
//                it.context.openFileOutput(filename, Context.MODE_PRIVATE).use {
//                    it.write(song.toString().toByteArray())
//                }
//                Handle_UI().toastWithDuration("Lưu thành công!",toastDuration,it.context.applicationContext)
//            }
//
            val folderName = HandleMemoryStorage.getInstance().faveSongFolderName
            HandleMemoryStorage.getInstance().saveFaveSongIntoFolder_InternalStorage(song, folderName.toString(),it.context)
        }
    }


}