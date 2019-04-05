package com.example.androidkaraokeapp.ulti

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.model.SongModel
import java.io.File


@SuppressLint("StaticFieldLeak")
object KaraokeMediaPlayer {
    private var mediaPlayer = MediaPlayer()
    private var mediaRecorder = MediaRecorder()

    var isPlaying: Boolean = false
    var isRecording: Boolean = false

    private lateinit var recordSavePath: String

    private var currentPlayPosition: Int = -1
    private var currentTime:Long = -1

    private lateinit var song: SongModel
    private lateinit var view: View

    private lateinit var playImageButton: ImageButton
    private lateinit var micImageButton: ImageButton
    private lateinit var nameSongTextView: TextView
    private lateinit var currentTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var durationSeekBar: SeekBar




    private val recordFirebaseCollection = FirestoreUlti.getInstance().db.collection(FirestoreUlti.Collection_Record)

    //region method private
    private fun configureUI() {
        micImageButton = view.findViewById(R.id.mic_image_button)
        playImageButton = view.findViewById(R.id.play_image_button)
        nameSongTextView = view.findViewById(R.id.name_song_text_view)
        currentTextView = view.findViewById(R.id.current_text_view)
        durationTextView = view.findViewById(R.id.duration_text_view)
        durationSeekBar = view.findViewById(R.id.duration_seek_bar)
    }

    private fun startTrackingPositionMedia() {
        Thread(Runnable {
            do {

                currentTextView.post {
                    Log.d("thread media", mediaPlayer.currentPosition.toString())
                    durationSeekBar.progress = mediaPlayer.currentPosition
                    currentPlayPosition = mediaPlayer.currentPosition
                    currentTextView.text = HandleDateTime.miliSecondToTime(currentPlayPosition.toLong())
                }
                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            } while (mediaPlayer.isPlaying)
        }).start()
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
        mediaRecorder.reset()
        val folderSavePath = view.context.applicationContext.filesDir.path + "/record"
        val file = File(folderSavePath)

        if (!file.exists()){
            file.mkdirs();
        }

        currentTime = System.currentTimeMillis()
        recordSavePath = "/${"Duy"}_${song.id}_${song.alias}_$currentTime.mp3"
        val recordOutput = file.path + recordSavePath

        mediaPlayer.setDataSource(song.mp3_url)

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setOutputFile(recordOutput)

        mediaPlayer.prepare()
        mediaRecorder.prepare()

        durationSeekBar.max = mediaPlayer.duration
        isPlaying = mediaPlayer.isPlaying
        currentPlayPosition = mediaPlayer.currentPosition
        val duration = HandleDateTime.miliSecondToTime(mediaPlayer.duration.toLong())
        durationTextView.text = duration
    }

    //endregion

    //region method public
    fun init(mView: View, inputSong: SongModel) {
        song = inputSong
        view = mView
        configureUI()
        prepare()
    }

    fun play() {
        playImageButton.setImageResource(R.drawable.ic_pause_black_36dp)
        mediaPlayer.seekTo(currentPlayPosition)

        mediaPlayer.start()
        startRecording()

        isPlaying = true
        isRecording = true

        startTrackingPositionMedia()
    }

    fun stop() {
        playImageButton.setImageResource(R.drawable.ic_play_arrow_black_36dp)
        currentPlayPosition = mediaPlayer.currentPosition

        mediaPlayer.stop()
        stopRecording()

        isPlaying = false
        isRecording = false

    }

    fun saveRecord() {
        val record = RecordModel(song.id, song.name, "Duy",recordSavePath, song.thumbnail_url, 123213, currentTime)

        recordFirebaseCollection.add(record)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    fun abort() {

        stop()
        val abortRecordingFile = view.context.applicationContext.filesDir.path + "/record" + recordSavePath
        val file = File(abortRecordingFile)

        if ( file.exists() )
            file.delete()

    }

}