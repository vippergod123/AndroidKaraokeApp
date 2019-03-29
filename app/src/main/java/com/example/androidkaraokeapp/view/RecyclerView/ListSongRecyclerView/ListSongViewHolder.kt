package com.example.androidkaraokeapp.view.RecyclerView.ListSongRecyclerView

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.FirestoreUlti
import com.example.androidkaraokeapp.ulti.RoundedTransformation
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

        Picasso.get().load(song.thumbnail_url).transform(RoundedTransformation(20f,0f))
            .into(songThumbnailImageView)





        itemView.setOnClickListener {
//            val intent = Intent(it.context, PrepareSongActivity::class.java)
//            intent.putExtra("song", song.toString())
            val intent = PrepareSongActivity.newIntent(it.context.applicationContext, song)
            it.context.startActivity(intent)
        }

    }

    fun setupFavoriteImageButton(listSong: MutableList<SongModel>){
        favoriteSongImageButton.setOnClickListener { it ->
            //            val folderName = HandleMemoryStorage.getInstance().faveSongFolderName
//            HandleMemoryStorage.getInstance().saveFaveSongIntoFolder_InternalStorage(song, folderName,it.context)
            val db = FirestoreUlti.getInstance().db
            val favSongCollection = FirestoreUlti.Collection_Favorite_Song
            val songCollection = FirestoreUlti.Collection_SONG
            val context = it.context


//            db.collection(songCollection).get()
//                .addOnSuccessListener { result ->
//                    val temp:MutableList<SongModel> = mutableListOf()
//                    for (document in result) {
//                        val song = document.toObject(SongModel::class.java )
//                        temp.add(song)
//                    }
//
//                }
//            db.collection(favSongCollection).add(song)
//                .addOnSuccessListener { Log.d("Add firestore", "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { e -> Log.w("Add firestore", "Error writing document", e) }
        }
    }


}