package com.example.androidkaraokeapp.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.view.recyclerView.ListShortRecordRecyclerView.ListShortRecordRecyclerViewAdapter

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PrepareSongActivity : AppCompatActivity() {

    private lateinit var listShortRecordRecyclerView: RecyclerView

    companion object {

        private val INTENT_SONG = "intent_song"

        fun newIntent(context: Context, song: SongModel): Intent {
            val intent = Intent(context, PrepareSongActivity::class.java)
            intent.putExtra(INTENT_SONG, song.name)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare_song)
        val song = intent.getStringExtra("song")
        configureUI()
    }


    private fun configureUI () {
        listShortRecordRecyclerView = findViewById(R.id.list_short_record_recycler_view)
        listShortRecordRecyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
        listShortRecordRecyclerView.adapter =  ListShortRecordRecyclerViewAdapter()

        //        Add divider to viewholder
        val dividerItemDecoration = DividerItemDecoration(this.applicationContext, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(this.applicationContext.resources.getDrawable(R.drawable.divider_recycler_view))
        listShortRecordRecyclerView.addItemDecoration(dividerItemDecoration)
    }
}
