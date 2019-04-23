package com.example.androidkaraokeapp.ulti

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.LyricModel
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.view.KaraokeScreenActivity
import com.example.androidkaraokeapp.view.custom_view.LyricTextView
import java.io.File


@SuppressLint("StaticFieldLeak")
object KaraokeMediaPlayer {
    private var mediaPlayer = MediaPlayer()
    private var mediaRecorder = MediaRecorder()

    var isPlaying: Boolean = false
    var isRecording: Boolean = false
    var isInit: Boolean = false

    private lateinit var recordSavePath: String

    private var currentPlayPosition: Int = -1
    private var currentCreateTime:Long = -1

    private var currentIndexKaraokeLyric:Int = -1
    private var nextIndexKaraokeLyric:Int = 0

    lateinit var playingMode:String
    private lateinit var karaokeTrackingRunnable :Runnable

    private var karaokeLyric: MutableList<LyricModel> = mutableListOf()

    private lateinit var song: SongModel
    private lateinit var view: View

    private lateinit var playImageButton: LottieAnimationView
    private lateinit var micImageButton: LottieAnimationView
    private lateinit var nameSongTextView: TextView
    private lateinit var currentTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var durationSeekBar: SeekBar
    private lateinit var lyricTopTextView: LyricTextView
    private lateinit var lyricBotTextView: TextView

    private val recordFirebaseCollection = FirestoreUlti.getInstance().db.collection(FirestoreUlti.Collection_Record)

    interface MediaPlayerFinishListener {
        fun finishActivity()
        fun finishPrepareKaraoke()
    }
    private var mediaPlayerFinishListener: MediaPlayerFinishListener ?= null

    //region method private
    private fun configureUI() {
        micImageButton = view.findViewById(R.id.mic_image_button)
        playImageButton = view.findViewById(R.id.play_image_button)

        nameSongTextView = view.findViewById(R.id.name_song_text_view)
        currentTextView = view.findViewById(R.id.current_text_view)
        durationTextView = view.findViewById(R.id.duration_text_view)
        lyricTopTextView = view.findViewById(R.id.lyric_top_text_view)
        lyricBotTextView = view.findViewById(R.id.lyric_bot_text_view)

        durationSeekBar = view.findViewById(R.id.duration_seek_bar)

        playImageButton.apply {
            speed = -2.5f
            useHardwareAcceleration(true)
        }


        when (playingMode) {
            KaraokeScreenActivity.MODE_RECORD,KaraokeScreenActivity.MODE_KARAOKE_TEST->{
                if (playingMode == KaraokeScreenActivity.MODE_RECORD)
                    micImageButton.visibility = View.INVISIBLE

                durationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        println("onProgressChanged")
                        currentPlayPosition = progress
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        println("onStartTrackingTouch")
                    }
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        println("onStopTrackingTouch")
                        mediaPlayer.seekTo(currentPlayPosition)
                        nextIndexKaraokeLyric = -1
                        lyricTopTextView.isRunning = false
                        startTrackingPositionMedia()
                    }
                })
            }
            KaraokeScreenActivity.MODE_KARAOKE->{
                durationSeekBar.setOnSeekBarChangeListener(null)
            }

        }

        mediaPlayer.setOnCompletionListener {
            stop()
            mediaPlayerFinishListener?.finishActivity()
        }
    }


    private fun setupKaraokeTrackingRunnable() {

        karaokeTrackingRunnable = Runnable {
            do {
                try {
                    currentTextView.post {

                        durationSeekBar.progress = mediaPlayer.currentPosition
                        currentPlayPosition = mediaPlayer.currentPosition
                        currentTextView.text = HandleDateTime.miliSecondToTime(currentPlayPosition.toLong())
                        if ( !lyricTopTextView.isRunning && nextIndexKaraokeLyric < karaokeLyric.size - 1) {

                            if ( nextIndexKaraokeLyric - currentIndexKaraokeLyric != 1) {
                                findIndexPlayingLyric(mediaPlayer.currentPosition)
                            }
                            else {
                                currentIndexKaraokeLyric ++
                            }

                            if ( currentIndexKaraokeLyric == - 1) {
                                currentIndexKaraokeLyric = karaokeLyric.size - 2
                            }

                            nextIndexKaraokeLyric = currentIndexKaraokeLyric + 1

                            val playingLyric = karaokeLyric[currentIndexKaraokeLyric]
                            val nextLyric = karaokeLyric[nextIndexKaraokeLyric]

                            lyricBotTextView.text =  nextLyric.text
                            lyricTopTextView.setKaraokeLyric(playingLyric, currentPlayPosition, mediaPlayer.isPlaying)

                        }
                    }
                    try {
                        Thread.sleep(lyricTopTextView.framePerSecond)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                catch (ex: Exception) {
                    Log.d("Ex", ex.toString())
                }
            } while (mediaPlayer.isPlaying)
        }
    }

    private fun startTrackingPositionMedia() {
        lyricTopTextView.visibility = View.VISIBLE
        lyricBotTextView.visibility = View.VISIBLE
        setupKaraokeTrackingRunnable()
        Thread(karaokeTrackingRunnable).start()

    }

    private fun findIndexPlayingLyric(currentPlayPosition:Int) {
        val temp = karaokeLyric.find {
            it.from <= currentPlayPosition && currentPlayPosition <= it.to
        }
        currentIndexKaraokeLyric = karaokeLyric.indexOf(temp)
    }


    private fun startRecording() {
        mediaRecorder.start()
        Handle_UI().toastWithDuration("Start recording", 1, view.context.applicationContext)
    }

    private fun stopRecording() {
        mediaRecorder.stop()
        Handle_UI().toastWithDuration("Stop recording", 1, view.context.applicationContext)
    }

    private fun prepare() {
        mediaPlayer.reset()

        val folderSavePath = view.context.applicationContext.filesDir.path + "/record"
        val file = File(folderSavePath)

        currentCreateTime = System.currentTimeMillis()
        recordSavePath = "/${"Duy"}_${song.id}_${song.alias}_$currentCreateTime.mp3"
        val recordOutput = file.path + recordSavePath

        var pathSource = song.mp3_url
        if ( playingMode == KaraokeScreenActivity.MODE_RECORD)
            pathSource = file.path + (song as RecordModel).record_url
        mediaPlayer.setDataSource(pathSource)
        mediaPlayer.prepare()

        if ( playingMode == KaraokeScreenActivity.MODE_KARAOKE) {

            if (!file.exists()){
                file.mkdirs()
            }
            mediaRecorder.reset()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder.setOutputFile(recordOutput)
            mediaRecorder.prepare()
        }


        durationSeekBar.max = mediaPlayer.duration
        isPlaying = mediaPlayer.isPlaying
        currentPlayPosition = mediaPlayer.currentPosition
        val duration = HandleDateTime.miliSecondToTime(mediaPlayer.duration.toLong())
        durationTextView.text = duration
    }

    private fun deleteRecordingSaveFile () {
        val abortRecordingFile = view.context.applicationContext.filesDir.path + "/record" + recordSavePath
        val file = File(abortRecordingFile)
        if (file.exists())
            file.delete()
    }
    //endregion

    //region method public
    fun init(mView: View, inputSong: SongModel, mode:String, listener: MediaPlayerFinishListener) {
        song = inputSong
        view = mView
        playingMode = mode
        mediaPlayerFinishListener = listener
        isInit = true

        configureUI()

        prepare()
    }

    fun play() {
//        playImageButton.reverseAnimationSpeed()
        playImageButtonAnimation()
        mediaPlayer.seekTo(currentPlayPosition)
        mediaPlayer.start()

        if ( playingMode == KaraokeScreenActivity.MODE_KARAOKE) {
            startRecording()
            isRecording = true
        }

        isPlaying = true
        startTrackingPositionMedia()
    }

    fun stop() {
//        playImageButton.setImageResource(R.drawable.ic_play_arrow_black_36dp)
//        playImageButton.playAnimation()
        playImageButtonAnimation()
        currentPlayPosition = mediaPlayer.currentPosition
        mediaPlayer.stop()

        if ( playingMode == KaraokeScreenActivity.MODE_KARAOKE)
            stopRecording()
        else
            deleteRecordingSaveFile()

        isPlaying = false
        isRecording = false
    }

    fun pause() {
        //Using when in Recorded Mode
//        karaokeTrackingThread.interrupt()
//        playImageButton.frame = 30
        playImageButtonAnimation()
        currentPlayPosition = mediaPlayer.currentPosition
        isPlaying = false
        lyricTopTextView.isRunning = isPlaying
        mediaPlayer.pause()


    }

    fun resume() {
        //Using when in Recorded Mode
//        playImageButton.frame = 30
//        playImageButton.reverseAnimationSpeed()
        playImageButtonAnimation()
        mediaPlayer.seekTo(currentPlayPosition)
        isPlaying = true
        mediaPlayer.start()
        nextIndexKaraokeLyric = -1
        startTrackingPositionMedia()

    }

    fun saveRecord() {
        val record = RecordModel(song.id,song.name,song.sub_url,song.thumbnail_url,song.alias,"Duy Khá bảnh", recordSavePath, currentCreateTime)

        recordFirebaseCollection.document(currentCreateTime.toString()).set(record)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    fun abort() {
            stop()
            deleteRecordingSaveFile()
    }


    fun getKaraokeLyric(mKaraokeLyric: MutableList<LyricModel>) {
        karaokeLyric = mKaraokeLyric
    }

    fun reset() {

        currentPlayPosition = -1

        currentIndexKaraokeLyric = -1
        nextIndexKaraokeLyric = 0

        try {
            lyricTopTextView.isRunning = false
            lyricTopTextView.visibility = View.INVISIBLE
            lyricBotTextView.visibility = View.INVISIBLE
            isInit = false
        }
        catch (ex: Exception) {

        }
    }

    private fun playImageButtonAnimation(){
        playImageButton.apply {
            speed = -speed
            setMinAndMaxFrame(0, 30)
            if (!isAnimating) {
                playAnimation()
            }
        }
    }
    //endregion
}