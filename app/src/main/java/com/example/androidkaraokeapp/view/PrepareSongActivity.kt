package com.example.androidkaraokeapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.view.recyclerView.ListShortRecordRecyclerView.ListShortRecordRecyclerViewAdapter
import com.squareup.picasso.Picasso


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PrepareSongActivity : AppCompatActivity() {

    private lateinit var listShortRecordRecyclerView: RecyclerView
    private lateinit var songDurationTextView: TextView
    private lateinit var songNameTextView: TextView
    private lateinit var userSingTextView: TextView
    private lateinit var thumbnailImageView: ImageView
    private lateinit var songDetailFrameLayout: FrameLayout
    private var song: SongModel ?= SongModel()

    companion object {

        const val BUNDLE_PREPARE_SONG = "bundle_prepare_song"

        fun newIntent(context: Context, song: SongModel): Intent {
            val intent = Intent(context, PrepareSongActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_PREPARE_SONG,song)           // Truyền một Boolean
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare_song)
        getDataFromBundle()
        configureUI()
        setupListener()
    }


    @SuppressLint("SetTextI18n")
    private fun configureUI () {
        listShortRecordRecyclerView = findViewById(R.id.list_short_record_recycler_view)
        songDurationTextView = findViewById(R.id.song_duration_text_view)
        songNameTextView = findViewById(R.id.song_name_text_view)
        userSingTextView = findViewById(R.id.singer_text_view)
        thumbnailImageView = findViewById(R.id.thumbnail_image_view)
        songDetailFrameLayout = findViewById(R.id.song_detail_frame_layout)


        songNameTextView.text = song!!.name
        userSingTextView.text = "Singer: ${song!!.singer}"
        Picasso.get().load(song!!.thumbnail_url).fit().into(thumbnailImageView)

        listShortRecordRecyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
        listShortRecordRecyclerView.adapter =  ListShortRecordRecyclerViewAdapter()

        //        Add divider to viewholder
        val dividerItemDecoration = DividerItemDecoration(this.applicationContext, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(this.applicationContext.resources.getDrawable(R.drawable.divider_recycler_view))
        listShortRecordRecyclerView.addItemDecoration(dividerItemDecoration)
    }


    private fun getDataFromBundle() {
        val bundle = intent.extras
        if (bundle != null) {
           song = bundle.getSerializable(BUNDLE_PREPARE_SONG) as SongModel
        }
    }

    private fun setupListener() {
        songDetailFrameLayout.setOnClickListener {
            val intent = RecordingFullscreenActivity.newIntent(it.context.applicationContext, song!!)
            startActivity(intent)
        }
    }
}
