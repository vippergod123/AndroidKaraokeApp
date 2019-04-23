package com.example.androidkaraokeapp.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.LyricModel
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.HandleDateTime
import com.example.androidkaraokeapp.ulti.KaraokeMediaPlayer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recording_fullscreen.*
import okhttp3.*
import java.io.IOException
import kotlin.math.round
import kotlin.random.Random
import android.content.DialogInterface
import android.app.AlertDialog


class KaraokeScreenActivity : AppCompatActivity(), KaraokeMediaPlayer.MediaPlayerFinishListener {



    private var karaokeLyric: MutableList<LyricModel> = mutableListOf()

    private lateinit var nameSongTextView: TextView
    private lateinit var countDownTextView: TextView
    private lateinit var backImageButton: ImageButton
    private lateinit var playImageButton: LottieAnimationView
    private lateinit var micImageButton: LottieAnimationView

    private lateinit var fullScreenContent: FrameLayout
    private lateinit var fullScreenContentControl: FrameLayout

    private lateinit var song: SongModel
    private val KaraokeHandler = Handler()
    private lateinit var playingMode: String


    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {

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

        private val AUTO_HIDE = true

        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        private const val UI_ANIMATION_DELAY = 300

        const val BUNDLE_KARAOKE_SONG = "bundle_karaoke_song"
        const val BUNDLE_KARAOKE_MODE = "bundle_kaorake_mode"

        const val MODE_KARAOKE = "MODE_KARAOKE"
        const val MODE_KARAOKE_TEST = "MODE_KARAOKE_TEST"
        const val MODE_RECORD = "MODE_RECORD"

        fun newIntent(context: Context, song: SongModel, mode:String): Intent {
            val intent = Intent(context, KaraokeScreenActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_KARAOKE_SONG,song)
            bundle.putString(BUNDLE_KARAOKE_MODE,mode)
            intent.putExtras(bundle)
            return intent
        }

        fun newIntentRecord(context: Context, record: RecordModel, mode:String): Intent  {
            val intent = Intent(context, KaraokeScreenActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_KARAOKE_SONG,record)
            bundle.putString(BUNDLE_KARAOKE_MODE,mode)
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

    override fun onDestroy() {
        super.onDestroy()
        KaraokeMediaPlayer.reset()
    }

    override fun finishActivity() {
        finish()
    }

    override fun finishPrepareKaraoke() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    private fun setBackgroundEverySecond(duration:Long) {
        val backgroundDrawable = arrayListOf(  R.drawable.background_1,
                                                            R.drawable.background_2,
                                                            R.drawable.background_3,
                                                            R.drawable.background_4,
                                                            R.drawable.background_5)
        var count = 1
        Thread(Runnable {
            do {
                try {
                    fullScreenContent.post {
//                        Picasso.get().load(backgroundDrawable[count%3]).fit().into(background_image_view)
                        fullScreenContent.setBackgroundResource(backgroundDrawable[count%5])
                    }
                    count++
                    try {
                        Thread.sleep(duration * 1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } while (KaraokeMediaPlayer.isPlaying)
        }).start()
    }


    //endregion

    //region private method
    private fun getDataFromBundle(){
        val bundle = intent.extras
        if (bundle != null) {
            playingMode = bundle.getString(BUNDLE_KARAOKE_MODE) as String
            when (playingMode) {
                MODE_KARAOKE, MODE_KARAOKE_TEST -> {
                    song = bundle.getSerializable(BUNDLE_KARAOKE_SONG) as SongModel
                }
                MODE_RECORD -> {
                    song = bundle.getSerializable(BUNDLE_KARAOKE_SONG) as RecordModel
                }
            }
        }
    }

    private fun configureUI() {
        backImageButton = findViewById(R.id.back_image_button)
        nameSongTextView = findViewById(R.id.name_song_text_view)
        playImageButton = findViewById(R.id.play_image_button)
        micImageButton = findViewById(R.id.mic_image_button)
        countDownTextView = findViewById(R.id.count_down_text_view)

        fullScreenContent = findViewById(R.id.fullscreen_content)
        fullScreenContentControl = findViewById(R.id.fullscreen_content_controls)

        nameSongTextView.text = song.name

        val request = Request.Builder().url(song.sub_url).build()
        val client = OkHttpClient()

        if ( playingMode == MODE_RECORD)
            micImageButton.visibility = View.INVISIBLE

        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Failed", "asd")
            }
            override fun onResponse(call: Call, response: Response) {
                val  data = response.body()?.string()
                runOnUiThread {
                    handleLyricStringToLyricModel(data)
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun setupListener(){
        backImageButton.setOnClickListener {
            if ( KaraokeMediaPlayer.isRecording)
                abortKaraoke()
            else {
                KaraokeMediaPlayer.abort()
                finish()
            }
        }

        playImageButton.setOnClickListener {
            when (KaraokeMediaPlayer.isPlaying) {
                true -> {
                    // media playing ->  stop, play icon
//                    KaraokeMediaPlayer.pause()

//                    playImageButton.setImageResource(R.drawable.ic_play_arrow_black_36dp)

                    when (playingMode) {
                        MODE_KARAOKE -> {
                            KaraokeMediaPlayer.stop()
                            KaraokeMediaPlayer.saveRecord()
                            finish()
                        }
                        MODE_RECORD, MODE_KARAOKE_TEST -> {
                            KaraokeMediaPlayer.pause()
                        }
                    }

                }

                false -> {
                    // media stop ->  play, pause icon
//                    playImageButton.setImageResource(R.drawable.ic_pause_black_36dp)
//                    playImageButton.reverseAnimationSpeed()
                    setBackgroundEverySecond(10)
                    if ( !KaraokeMediaPlayer.isInit ) {
                        loading_text_view.visibility = View.VISIBLE
                        fullScreenContentControl.visibility = View.INVISIBLE
                        KaraokeHandler.postDelayed({
                            loading_text_view.visibility = View.INVISIBLE
                            KaraokeMediaPlayer.init(findViewById(android.R.id.content), song, playingMode, this)

                            when (playingMode) {
                                MODE_KARAOKE -> {
                                    countDownSeconds(4)
                                    KaraokeHandler.postDelayed({
                                        KaraokeMediaPlayer.play()
                                    },5500)
                                }
                                MODE_RECORD,MODE_KARAOKE_TEST -> {
                                    KaraokeMediaPlayer.play()
                                }
                            }
                        }, 100)
                    }
                    else if ( !KaraokeMediaPlayer.isPlaying )
                        when( playingMode) {
                            MODE_KARAOKE-> {

                            }
                            MODE_RECORD,MODE_KARAOKE_TEST -> {
                                KaraokeMediaPlayer.resume()
                            }
                        }


                }
            }
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }

        micImageButton.setOnClickListener {
            when (KaraokeMediaPlayer.isRecording) {
                false -> {
                    // stop ->  recording
                    micImageButton.playAnimation()
//                    playImageButton.setImageResource(R.drawable.ic_pause_black_36dp)

                    KaraokeMediaPlayer.playingMode = MODE_KARAOKE
                    playingMode = MODE_KARAOKE
//                    KaraokeMediaPlayer.reset()
                    loading_text_view.visibility = View.VISIBLE
                    fullScreenContentControl.visibility = View.INVISIBLE
                    KaraokeHandler.postDelayed({
                        loading_text_view.visibility = View.INVISIBLE
                        KaraokeMediaPlayer.init(findViewById(android.R.id.content), song, playingMode, this)
                        KaraokeMediaPlayer.reset()
                        countDownSeconds(4)
                        KaraokeHandler.postDelayed({
                            KaraokeMediaPlayer.play()
                        },5500)

                    }, 100)
                }
                true -> {
                    // recording ->  stop
//                    playImageButton.setImageResource(R.drawable.ic_play_arrow_black_36dp)
//                    playImageButton.reverseAnimationSpeed()
                    KaraokeMediaPlayer.stop()
                    if (playingMode == MODE_KARAOKE)
                        KaraokeMediaPlayer.saveRecord()
                    finish()
                }
            }

        }
    }


    private fun abortKaraoke() {
        when( playingMode) {
            MODE_KARAOKE->{
//                val dialog = Dialog(this)
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                dialog.setCancelable(false)
//                dialog.setContentView(R.layout.kaorake_abort_dialog)
//
//                val abortButton = dialog.findViewById(R.id.karaoke_abort_button) as Button
//                val cancleButton = dialog.findViewById(R.id.karaoke_cancel_button) as Button
//                cancleButton.setOnClickListener { dialog.dismiss() }
//                abortButton.setOnClickListener{
//                    KaraokeMediaPlayer.abort()
//                    finish()
//                }
//                dialog.show()
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Đang thu âm bài hát")
                builder.setMessage("Bạn vẫn muốn thoát khỏi trình thu âm?")

                builder.setPositiveButton("Tiếp tục") { dialog, _ ->
                    dialog.dismiss()
                }

                builder.setNegativeButton("Thoát") { dialog, _ ->
                    KaraokeMediaPlayer.abort()
                    finish()
                }
                val alert = builder.create()
                alert.show()
            }
            MODE_RECORD, MODE_KARAOKE_TEST-> {
                KaraokeMediaPlayer.abort()
                finish()
            }
        }



    }


    private  fun handleLyricStringToLyricModel(data:String?) {
//        val  lyric = response.body()?.string()
        val lyric = data!!.split("\r\n").toMutableList()
        lyric.removeAll {
            it == ""
        }

        for ( i in 0 until lyric.lastIndex) {
            val temp = LyricModel()

            try {
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

        // add last lyric to made song look better at the end - needed
        val temp = karaokeLyric[karaokeLyric.lastIndex]
        var addLastLyric = LyricModel("Kết thúc bài hát !",temp.to,temp.to + 3000F)
        karaokeLyric.add(addLastLyric)
        addLastLyric = LyricModel("",temp.to,temp.to + 3000F)
        karaokeLyric.add(addLastLyric)

        KaraokeMediaPlayer.getKaraokeLyric(karaokeLyric)
    }
    //endregion

    private fun countDownSeconds (duration: Long ) {
        countDownTextView.visibility = View.VISIBLE
        object: CountDownTimer(duration * 1000,900) {
            override fun onTick(millisUntilFinished: Long) {
                countDownTextView.text = round(millisUntilFinished.toFloat()/1000).toInt().toString()
            }

            override fun onFinish() {
                countDownTextView.text = "Bắt đầu thu..."
                KaraokeHandler.postDelayed({countDownTextView.visibility = View.INVISIBLE}, 1000)
            }
        }.start()
    }
}
