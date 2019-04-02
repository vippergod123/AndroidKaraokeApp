package com.example.androidkaraokeapp.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import java.lang.ClassCastException


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNREACHABLE_CODE")

class ListSongDialogFragment: DialogFragment() {

    private lateinit var addToFavoriteButton: ConstraintLayout
    private lateinit var downloadButton: ConstraintLayout

    interface ListSongDialogFragmentListener {
        fun sendBackRequestCode(code:String)
    }
    private  var listSongDialogFragmentListener: ListSongDialogFragmentListener ?= null

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: ListSongDialogFragment? = null

        const val REQUEST_CODE_ADD_FAVORITE = "add_favorite"
        const val REQUEST_CODE_DOWNLOAD_SONG = "download_song"

        fun getInstance(listener: ListSongDialogFragmentListener) = instance ?: synchronized(this) {
            instance ?: ListSongDialogFragment().also {
                it.listSongDialogFragmentListener = listener
            }
        }
    }
    //region Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_song_dialog_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.let{
            configureUI(it)
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window.setLayout(width, height)
        }

    }
    //endregion

    private fun configureUI(view:View) {
        addToFavoriteButton = view.findViewById(R.id.add_to_favorite_button)
        downloadButton = view.findViewById(R.id.download_button)

        addToFavoriteButton.setOnClickListener {
            listSongDialogFragmentListener!!.sendBackRequestCode(REQUEST_CODE_ADD_FAVORITE)

            dialog.dismiss()
        }
        downloadButton.setOnClickListener {
            listSongDialogFragmentListener!!.sendBackRequestCode(REQUEST_CODE_DOWNLOAD_SONG)
            dialog.dismiss()
        }
    }

}