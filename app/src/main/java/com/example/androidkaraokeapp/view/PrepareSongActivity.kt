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
import android.media.MediaMetadataRetriever
import android.os.AsyncTask
import com.example.androidkaraokeapp.ulti.HandleDateTime
import android.os.Build
import android.view.View
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.presenter.ListRecordContract
import com.example.androidkaraokeapp.presenter.ListRecordPresenter
import com.example.androidkaraokeapp.view.recyclerView.ListRecordRecyclerView.ListRecordRecyclerViewAdapter


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PrepareSongActivity : AppCompatActivity(), ListRecordContract.View {


    private lateinit var listShortRecordRecyclerView: RecyclerView
    private lateinit var songDurationTextView: TextView
    private lateinit var songNameTextView: TextView
    private lateinit var userSingTextView: TextView
    private lateinit var thumbnailImageView: ImageView
    private lateinit var songDetailFrameLayout: FrameLayout


    private lateinit var listRecordAdapter: ListShortRecordRecyclerViewAdapter

    private var listRecordPresenter = ListRecordPresenter()
    private var listRecord: MutableList<RecordModel> = mutableListOf()
    private var song: SongModel = SongModel()

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

    //region override method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare_song)
        getDataFromBundle()
        configureUI()
        setupPresenter()
        setupListener()

    }


    @SuppressLint("SetTextI18n")

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
//        listShortRecordRecyclerView.adapter =  ListShortRecordRecyclerViewAdapter(this,song!!.id)
        songDurationTextView.text = "Thời gian: đang xử lý..."
        val backGroundTask = CalculateDurationSong(song.mp3_url, callBack  = {
            songDurationTextView.text = "Thời gian: $it"
        })
        backGroundTask.execute()

    }

    override fun onResume() {
        super.onResume()
        listRecordPresenter.findRecordBySong(listRecord,song)
    }

    override fun showListRecord() {
        listShortRecordRecyclerView.adapter = ListShortRecordRecyclerViewAdapter(listRecord)
    }

    override fun updateListRecord(removeRecord: RecordModel) {
        println()
    }

    //endregion

    //region private method
    @SuppressLint("SetTextI18n")
    private fun configureUI () {
        listShortRecordRecyclerView = findViewById(R.id.list_short_record_recycler_view)
        songDurationTextView = findViewById(R.id.song_duration_text_view)
        songNameTextView = findViewById(R.id.song_name_text_view)
        userSingTextView = findViewById(R.id.singer_text_view)
        thumbnailImageView = findViewById(R.id.thumbnail_image_view)
        songDetailFrameLayout = findViewById(R.id.song_detail_frame_layout)


        songNameTextView.text = song.name
        userSingTextView.text = "Singer: ${song.singer}"
        Picasso.get().load(song.thumbnail_url).fit().into(thumbnailImageView)



        listShortRecordRecyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
        listShortRecordRecyclerView.adapter =  ListShortRecordRecyclerViewAdapter(listRecord)
        listRecordAdapter = listShortRecordRecyclerView.adapter as ListShortRecordRecyclerViewAdapter

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
            val intent = KaraokeScreenActivity.newIntent(it.context.applicationContext, song, KaraokeScreenActivity.MODE_KARAOKE)
            startActivity(intent)
        }
    }

    private fun setupPresenter() {
        listRecordPresenter.setView(this)
    }
    //endregion

}


private class CalculateDurationSong(private val imageURL:String, var callBack: (String) -> Unit): AsyncTask<String,Void,String>() {
    override fun doInBackground(vararg params: String?): String {
        val mmr = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 14)
                mmr.setDataSource(imageURL, HashMap<String, String>())
            else
                mmr.setDataSource(imageURL)
            val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
//        mmr.release()
            return HandleDateTime.miliSecondToTime(duration.toLong())
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        callBack(result)
    }
}