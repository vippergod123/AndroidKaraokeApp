package com.example.androidkaraokeapp.view.recyclerView.ListSongRecyclerView

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.RoundedTransformation
import com.example.androidkaraokeapp.view.dialog.ListSongDialogFragment
import com.example.androidkaraokeapp.view.PrepareSongActivity
import com.squareup.picasso.Picasso
import android.util.Log
import com.example.androidkaraokeapp.ulti.FirestoreUlti
import com.example.androidkaraokeapp.ulti.Handle_UI
import com.example.androidkaraokeapp.view.MainActivity


class ListSongViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!), ListSongDialogFragment.ListSongDialogFragmentListener{

    private val songNameTextView = itemView?.findViewById(R.id.song_name_text_view) as TextView
    private val songSingerTextView = itemView?.findViewById(R.id.song_singer_text_view) as TextView
    private val favoriteSongImageButton = itemView?.findViewById(R.id.favorite_song_image_button) as ImageButton
    private val songThumbnailImageView = itemView?.findViewById<ImageView>(R.id.song_thumbnail_image_view)

    private lateinit var dialog : ListSongDialogFragment

    private lateinit var song:SongModel

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

    fun setupFavoriteImageButton(inputSong: SongModel){
        favoriteSongImageButton.setOnClickListener { it ->
//            //            val folderName = HandleMemoryStorage.getInstance().faveSongFolderName
////            HandleMemoryStorage.getInstance().saveFaveSongIntoFolder_InternalStorage(song, folderName,it.context)
////            val db = FirestoreUlti.getInstance().db
////            val favSongCollection = FirestoreUlti.Collection_Favorite_Song
////            val songCollection = FirestoreUlti.Collection_SONG
////            val context = it.context
////            db.collection(songCollection).get()
////                .addOnSuccessListener { result ->
////                    val temp:MutableList<SongModel> = mutableListOf()
////                    for (document in result) {
////                        val song = document.toObject(SongModel::class.java )
////                        temp.add(song)
////                    }
////
////                }
////            db.collection(favSongCollection).add(song)
////                .addOnSuccessListener { Log.d("Add firestore", "DocumentSnapshot successfully written!") }
////                .addOnFailureListener { e -> Log.w("Add firestore", "Error writing document", e) }
//            song = inputSong
            song = inputSong
            Log.d("dialog", song.name)
            dialog = ListSongDialogFragment.getInstance(this)
            val fragmentManager = (itemView.context as MainActivity).supportFragmentManager

            val ft = fragmentManager.beginTransaction()
            val prevFragment = fragmentManager.findFragmentByTag("dialog")
            if (prevFragment != null) {
                ft.remove(prevFragment)
            }
            ft.addToBackStack(null)
            dialog.show(ft,"dialog")

        }
    }

    override fun sendBackRequestCode(code: String) {
        when(code) {
            ListSongDialogFragment.REQUEST_CODE_DOWNLOAD_SONG -> {
                downloadSongToMemory(song)
            }
            ListSongDialogFragment.REQUEST_CODE_ADD_FAVORITE -> {
                addSongToFavorite(song)
            }
        }
    }

    private fun addSongToFavorite(song:SongModel) {
        Log.d("dialog add", song.name)
        val db = FirestoreUlti.getInstance().db
        val favSongCollection = db.collection(FirestoreUlti.Collection_Favorite_Song)

        favSongCollection.whereEqualTo("name", song.name).get().addOnSuccessListener { result ->
            if (result.size() > 0) {
                Handle_UI().toastWithDuration("Bài hát đã được lưu", 1, itemView.context)
            }
            else {
                favSongCollection.add(song).addOnSuccessListener { documentReference ->
                                                                    Handle_UI().toastWithDuration("Lưu thành công", 1, itemView.context)
                                                                }
                                                            .addOnFailureListener { e ->
                                                                    Handle_UI().toastWithDuration("Lưu không thành công", 1, itemView.context)
                                                                }

            }
        }
    }

    private fun downloadSongToMemory(song:SongModel) {
        Log.d("dialog download", song.name)

    }

}