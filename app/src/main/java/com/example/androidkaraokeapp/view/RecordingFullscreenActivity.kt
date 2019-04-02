package com.example.androidkaraokeapp.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.SongModel
import kotlinx.android.synthetic.main.activity_recording_fullscreen.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class RecordingFullscreenActivity : AppCompatActivity() {

    private lateinit var backImageButton: ImageButton
    private lateinit var playImageButton: ImageButton
    private lateinit var micImageButton: ImageButton
    private lateinit var nameSongTextView: TextView
    private lateinit var currentTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var durationSeekBar: SeekBar

    private lateinit var fullScreenContent: FrameLayout
    private lateinit var fullScreenContentControl: FrameLayout
    private var song: SongModel = SongModel()

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
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
        supportActionBar?.show()
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
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300


        const val BUNDLE_RECORDING_SONG = "bundle_recording_song"
        const val RECORDING_SONG_REQUEST_CODE = 100

        fun newIntent(context: Context, song: SongModel): Intent {
            val intent = Intent(context, RecordingFullscreenActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_RECORDING_SONG,song)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording_fullscreen)

        getDataFromBundle()
        configureUI()
        setupListener()

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        fullScreenContent.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        play_image_button.setOnTouchListener(mDelayHideTouchListener)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    //region private method
    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        fullScreenContentControl.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullScreenContent.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
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

    private fun getDataFromBundle(){
        val bundle = intent.extras
        if (bundle != null) {
            song = bundle.getSerializable(BUNDLE_RECORDING_SONG) as SongModel
        }
    }

    private fun configureUI() {
        micImageButton = findViewById(R.id.mic_image_button)
        backImageButton = findViewById(R.id.back_image_button)
        playImageButton = findViewById(R.id.play_image_button)
        nameSongTextView = findViewById(R.id.name_song_text_view)
        currentTextView = findViewById(R.id.current_text_view)
        durationTextView = findViewById(R.id.duration_text_view)
        durationSeekBar = findViewById(R.id.duration_seek_bar)

        fullScreenContent = findViewById(R.id.fullscreen_content)
        fullScreenContentControl = findViewById(R.id.fullscreen_content_controls)
        nameSongTextView.text = song.name

    }

    private fun setupListener(){
        backImageButton.setOnClickListener {
            finish()
        }
    }
    //endregion

}
