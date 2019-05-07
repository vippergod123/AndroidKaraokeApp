package com.example.androidkaraokeapp.view.recyclerView.ListSongRecyclerView

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.RoundedTransformation
import com.example.androidkaraokeapp.view.PrepareSongActivity
import com.squareup.picasso.Picasso
import com.airbnb.lottie.LottieAnimationView
import com.example.androidkaraokeapp.ulti.FirestoreUlti
import com.example.androidkaraokeapp.ulti.Handle_UI


class ListSongViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){

    private val songNameTextView = itemView?.findViewById(R.id.song_name_text_view) as TextView
    private val songSingerTextView = itemView?.findViewById(R.id.song_singer_text_view) as TextView
    private val songIdTextView = itemView?.findViewById(R.id.song_id_text_view) as TextView
    private val favoriteSongImageButton = itemView?.findViewById(R.id.favorite_song_image_button) as LottieAnimationView
    private val songThumbnailImageView = itemView?.findViewById<ImageView>(R.id.record_image_view)

    private lateinit var song:SongModel

    //region public method
    @SuppressLint("SetTextI18n")
    fun bind (mSong: SongModel) {
        song =  mSong
        songNameTextView.text = song.name
        songSingerTextView.text = song.singer
        songIdTextView.text = "Mã số: ${song.id}"

        Picasso.get().load(song.thumbnail_url).transform(RoundedTransformation(20f,0f))
            .into(songThumbnailImageView)

        setupListener()

        favoriteSongImageButton.speed = 2.5f
        if (song.isLiked){
            favoriteSongImageButton.frame = 140
        }
        else {
            favoriteSongImageButton.frame = 55
        }
        favoriteSongImageButton.useHardwareAcceleration(true)

    }


    fun setupFavoriteImageButton() {
        favoriteSongImageButton.setOnClickListener {
            if (!favoriteSongImageButton.isAnimating) {
                val db = FirestoreUlti.getInstance().db
                val songCollection = db.collection(FirestoreUlti.Collection_SONG)

                songCollection.document(song.id.toString()).update("isLiked", !song.isLiked)
                    .addOnSuccessListener {
//                        Handle_UI().toastWithDuration("Lưu thành công ${!song.isLiked} ", 1, itemView.context)
                        song.isLiked = !song.isLiked
                        favoriteSongImageButtonAnimation()
                    }
                    .addOnFailureListener {
                        Handle_UI().toastWithDuration("Thao tác thất bại!", 1, itemView.context)
                    }
            }
        }
    }
    //endregion

    //region private method
    private fun setupListener() {
        itemView.setOnClickListener {
            val intent = PrepareSongActivity.newIntent(it.context.applicationContext, song)
            it.context.startActivity(intent)
        }
    }

    private fun favoriteSongImageButtonAnimation(){
        if (!song.isLiked)
            favoriteSongImageButton.setMinAndMaxFrame(140, 150)
        else
            favoriteSongImageButton.setMinAndMaxFrame(55,140)
        favoriteSongImageButton.playAnimation()

    }
    //endregion
}