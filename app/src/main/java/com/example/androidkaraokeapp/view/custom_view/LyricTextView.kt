package com.example.androidkaraokeapp.view.custom_view

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import java.lang.Runnable as Runnable
import android.widget.Chronometer
import com.example.androidkaraokeapp.model.LyricModel

class LyricTextView: TextView {
    private var mHeight = 0
    private var mWidth = 0
    private var fontSize = 30f
    private var lyricText = "Textview"
    private var paint = Paint()
    private var isInit = false
    private var duration:Float = 500F
    private var currentMilis:Float = 0F
    private var strokeWidth = 10f
    private lateinit var chronometer: Chronometer

    private lateinit var canvas: Canvas

    var framePerSecond:Long = 1000/24
    var isRunning = false
    var isDrawCompleted = true

    private lateinit var lyric:LyricModel
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)



    private fun initView (mCanvas: Canvas?) {
        fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,fontSize, resources.displayMetrics)
        setupPaint()
        mHeight = height
        isInit = true
        chronometer = Chronometer(context)
        canvas= mCanvas!!
    }



    //region override method
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(mWidth,MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(150,MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if ( !isInit) {
            initView(canvas)
        }
        drawText(canvas)
    }


    //endregion

    //region private method

    private fun drawText(canvas: Canvas?):Void? {

            val bounds = Rect()
            paint.getTextBounds(lyricText, 0, lyricText.length, bounds)



            if (currentMilis <= duration && canvas !=null) {
                val ratio = currentMilis / duration

                val shader = LinearGradient(
                    mWidth.toFloat() * ratio,
                    mHeight.toFloat() / 2,
                    mWidth.toFloat() * ratio + 30F,
                    mHeight.toFloat() / 2,
                    Color.RED,
                    Color.WHITE,
                    Shader.TileMode.CLAMP
                )

                if ( currentMilis == 0F && duration == 0F) {
                    setupPaint()
                    canvas.drawText(lyricText, 0, lyricText.length , canvas.width/2.toFloat(), canvas.height/1.5.toFloat(), paint)
                    isRunning = false
                    return null
                }
                else {
                    paint.shader = shader
                    canvas.drawText(lyricText, 0, lyricText.length, canvas.width / 2.toFloat(), canvas.height / 1.5.toFloat(), paint)
                }
                if ( isRunning ){
                    currentMilis = (SystemClock.elapsedRealtime() - chronometer.base).toFloat()
                    postInvalidateDelayed(framePerSecond)
                }
            }
            else {
                reset()
            }
        return null
    }

    fun setKaraokeLyric(mLyric: LyricModel) {
        lyric = mLyric
        lyricText = mLyric.text

        duration = 0F
        currentMilis = 0F



        mWidth =  paint.measureText(lyricText, 0, lyricText.length).toInt()
//        isRunning = mediaPlayerIsPlaying

        requestLayout()
    }

    fun play(currentPlayPosition:Int){

        isRunning = true

        isDrawCompleted= false

        duration = lyric.to - currentPlayPosition

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        requestLayout()
    }

    private fun reset() {
        duration = 0F
        currentMilis = 0F
        isRunning = false
        isDrawCompleted= true
        paint.color = Color.WHITE
        chronometer.stop()
    }

    private fun setupPaint(){
        paint = Paint()
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textSize = fontSize
        paint.textAlign = Paint.Align.CENTER
        paint.strokeWidth = strokeWidth
        paint.color = Color.WHITE
    }
}