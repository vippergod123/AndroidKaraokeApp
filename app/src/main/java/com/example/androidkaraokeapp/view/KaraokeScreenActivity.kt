package com.example.androidkaraokeapp.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.LyricModel
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.HandleDateTime
import com.example.androidkaraokeapp.ulti.KaraokeMediaPlayer
import kotlinx.android.synthetic.main.activity_recording_fullscreen.*
import okhttp3.*
import java.io.IOException


class KaraokeScreenActivity : AppCompatActivity() {

    private var karaokeLyric: MutableList<LyricModel> = mutableListOf()

    private lateinit var backImageButton: ImageButton
    private lateinit var nameSongTextView: TextView
    private lateinit var playImageButton: ImageButton
    private lateinit var micImageButton: ImageButton


//    private lateinit var lyricTopTextView: LyricTextView
//    private lateinit var lyricBotTextView: LyricTextView

    private lateinit var fullScreenContent: FrameLayout
    private lateinit var fullScreenContentControl: FrameLayout


    private var song: SongModel = SongModel()


    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.s
        fullScreenContent.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
//        supportActionBar?.show()
        fullScreenContentControl.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false


    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300







        const val BUNDLE_KARAOKE_SONG = "bundle_recording_song"
        const val BUNDLE_KARAOKE_MODE = "bundle_recording_song"


        fun newIntent(context: Context, song: SongModel): Intent {
            val intent = Intent(context, KaraokeScreenActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_KARAOKE_SONG,song)
            intent.putExtras(bundle)
            return intent
        }
    }

    //region override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_recording_fullscreen)

        getDataFromBundle()
        configureUI()
        setupListener()

        mVisible = true

        fullScreenContent.setOnClickListener {
            toggle()
        }

        play_image_button.setOnTouchListener(mDelayHideTouchListener)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        KaraokeMediaPlayer.init(findViewById(android.R.id.content),song)
        KaraokeMediaPlayer.saveRecord()
        show()

    }

    //endregion

    //region private method UI
    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }

    }

    private fun hide() {
        // Hide UI first
        fullScreenContentControl.visibility = View.INVISIBLE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullScreenContent.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private val mHideRunnable = Runnable { hide() }

    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    //endregion

    //private methodX

    private fun getDataFromBundle(){
        val bundle = intent.extras
        if (bundle != null) {
            song = bundle.getSerializable(BUNDLE_KARAOKE_SONG) as SongModel
        }
    }

    private fun configureUI() {
        backImageButton = findViewById(R.id.back_image_button)
        nameSongTextView = findViewById(R.id.name_song_text_view)
        playImageButton = findViewById(R.id.play_image_button)
        micImageButton = findViewById(R.id.mic_image_button)

//        lyricTopTextView = findViewById(R.id.lyric_top_text_view)
//        lyricBotTextView = findViewById(R.id.lyric_bot_text_view)

        fullScreenContent = findViewById(R.id.fullscreen_content)
        fullScreenContentControl = findViewById(R.id.fullscreen_content_controls)

        nameSongTextView.text = song.name




        val request = Request.Builder().url(song.sub_url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Failed", "asd")
            }

            override fun onResponse(call: Call, response: Response) {
                val  body = response.body()?.string()
                val lyric = body!!.split("\r\n")
                for ( i in 0 until lyric.lastIndex) {
                    val temp = LyricModel()

                    try {

                        if ( lyric[i] == "") {
                            val to = HandleDateTime.timeToMiliSeconds(
                                lyric[i + 1].substring(
                                    lyric[i + 1].indexOf("[") + 1,
                                    lyric[i + 1].indexOf("]")
                                )
                            )
                            karaokeLyric[karaokeLyric.lastIndex].to = to
                            continue
                        }

                        val from = HandleDateTime.timeToMiliSeconds(
                            lyric[i].substring(
                                lyric[i].indexOf("[") + 1,
                                lyric[i].indexOf("]")
                            )
                        )
                        temp.from = from

                        val to = HandleDateTime.timeToMiliSeconds(
                            lyric[i + 1].substring(
                                lyric[i + 1].indexOf("[") + 1,
                                lyric[i + 1].indexOf("]")
                            )
                        )
                        temp.to = to

                        // jump when lyric mp3 zing === " "


                        val duration = temp.to - temp.from
                        var text = lyric[i].substring(lyric[i].indexOf("]") + 1, lyric[i].lastIndex + 1)

                        if ( text == "" ) {
                            if (duration > 4000) {
                                text = "Nhạc dạo"
                                temp.text = text
                                karaokeLyric.add(temp)
                            }
                            else {
                                karaokeLyric[karaokeLyric.lastIndex].to = to
                            }
                        }
                        else {
                            temp.text = text
                            karaokeLyric.add(temp)
                        }

                    }
                    catch(ex:Exception) {
                        println(ex)
                    }
                }
                KaraokeMediaPlayer.getKaraokeLyric(karaokeLyric)
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun setupListener(){
        backImageButton.setOnClickListener {
            if ( KaraokeMediaPlayer.isRecording)
                abortKaraoke()
            else {
                finish()
            }
        }

        playImageButton.setOnClickListener {
            when (KaraokeMediaPlayer.isPlaying) {
                true -> {
                    // media playing ->  stop, play icon
//                    KaraokeMediaPlayer.pause()
                    KaraokeMediaPlayer.stop()
                    finish()
                }

                false -> {
                    // media stop ->  play, pause icon
                    KaraokeMediaPlayer.play()
                }
            }
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }

        micImageButton.setOnClickListener {
            when (KaraokeMediaPlayer.isRecording) {
                true -> {
                    // recording ->  stop
                    KaraokeMediaPlayer.stop()
                    finish()
                }
            }

        }
    }


    private fun abortKaraoke() {


        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.kaorake_abort_dialog)

        val abortButton = dialog.findViewById(R.id.karaoke_abort_button) as Button
        val cancleButton = dialog.findViewById(R.id.karaoke_cancel_button) as Button
        cancleButton.setOnClickListener { dialog.dismiss() }
        abortButton.setOnClickListener{
            KaraokeMediaPlayer.abort()
            finish()
        }
        dialog.show()


//

    }
    //endregion

}
