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
import com.example.androidkaraokeapp.model.LyricModel
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.view.custom_view.LyricTextView
import java.io.File


@SuppressLint("StaticFieldLeak")
object KaraokeMediaPlayer {
    private var mediaPlayer = MediaPlayer()
    private var mediaRecorder = MediaRecorder()

    var isPlaying: Boolean = false
    var isRecording: Boolean = false

    private lateinit var recordSavePath: String

    private var currentPlayPosition: Int = -1
    private var currentCreateTime:Long = -1
    private var currentIndexKaraokeLyric:Int = -1
    private var karaokeLyric: MutableList<LyricModel> = mutableListOf()

    private lateinit var song: SongModel
    private lateinit var view: View

    private lateinit var playImageButton: ImageButton
    private lateinit var micImageButton: ImageButton
    private lateinit var nameSongTextView: TextView
    private lateinit var currentTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var durationSeekBar: SeekBar
    private lateinit var lyricTopTextView: LyricTextView
    private lateinit var lyricBotTextView: TextView



    private val recordFirebaseCollection = FirestoreUlti.getInstance().db.collection(FirestoreUlti.Collection_Record)

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

//        lyricTopTextView.setTextAndDuration("ta mai yeu ta hello helloeq weqw eqwqw ", 5000F)
        durationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                println("onProgressChanged")
//                if ((!lyricTopTextView.isRunning) && isPlaying) {
//                    val temp = karaokeLyric.find {
//                        it.from < progress && progress < it.to
//                    }
//                    lyricTopTextView.setTextAndDuration(temp?.text!!, temp.to - temp.from)
//                    lyricBotTextView.text = karaokeLyric[karaokeLyric.indexOf(temp) + 1].text
//                }


//
//                if (isPlaying) {
//                    startTrackingLyricKaraoke()
//                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                println("onStartTrackingTouch")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                println("onStopTrackingTouch")
//                val temp = karaokeLyric.find {
//                    it.from < currentPlayPosition && currentPlayPosition < it.to
//                }
//                lyricTopTextView.setTextAndDuration(temp?.text!!, temp.to - currentPlayPosition)
            }

        })
    }

    private fun startTrackingPositionMedia() {
        Thread(Runnable {
            do {

                try {
                    currentTextView.post {
                        Log.d("thread media", mediaPlayer.currentPosition.toString())
                        durationSeekBar.progress = mediaPlayer.currentPosition
                        currentPlayPosition = mediaPlayer.currentPosition
                        currentTextView.text = HandleDateTime.miliSecondToTime(currentPlayPosition.toLong())
                        if ( !lyricTopTextView.isRunning) {
                            val temp = karaokeLyric.find {
                                it.from <= currentPlayPosition && currentPlayPosition <= it.to
                            }

                            lyricBotTextView.text =  karaokeLyric[karaokeLyric.indexOf(temp) + 1].text
                            lyricTopTextView.setTextAndDuration(
                                temp?.text!!,
                                temp.to - mediaPlayer.currentPosition.toFloat()
                            )
                        }
                    }
//                    try {
//                        Thread.sleep(100)
//                    } catch (e: InterruptedException) {
//                        e.printStackTrace()
//
//                    }
                }
                catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } while (mediaPlayer.isPlaying)
        }).start()
    }

    private fun startTrackingLyricKaraoke () {
        Thread(Runnable {
            do {

                lyricTopTextView.post {
                    if ( !lyricTopTextView.isRunning) {



                        try {
//                            val temp = karaokeLyric.find {
//                                it.from < mediaPlayer.currentPosition && mediaPlayer.currentPosition < it.to
//                            }
//
//                            Log.d(
//                                "lyric",
//                                "${mediaPlayer.currentPosition} - ${temp?.text} - ${temp?.to}  - ${0 - mediaPlayer.currentPosition + temp?.to!!}"
//                            )
//
//                            lyricBotTextView.text = karaokeLyric[karaokeLyric.indexOf(temp) + 1].text
//                            lyricTopTextView.setTextAndDuration(
//                                temp?.text!!,
//                                temp.to - mediaPlayer.currentPosition.toFloat()
//                            )

                        }
                        catch( ex:Exception) {
                            Log.d("lyric ex: ", ex.toString())
                        }
                    }
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

        currentCreateTime = System.currentTimeMillis()
        recordSavePath = "/${"Duy"}_${song.id}_${song.alias}_$currentCreateTime.mp3"
        val recordOutput = file.path + recordSavePath

        mediaPlayer.setDataSource(song.mp3_url)
        mediaPlayer.prepare()

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setOutputFile(recordOutput)
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
        val record = RecordModel(song.id, song.name, "Duy",recordSavePath, song.thumbnail_url, 123213, currentCreateTime)

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


    fun getKaraokeLyric(mKaraokeLyric: MutableList<LyricModel>) {
        karaokeLyric = mKaraokeLyric


    }
}