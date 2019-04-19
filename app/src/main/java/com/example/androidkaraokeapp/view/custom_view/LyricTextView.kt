package com.example.androidkaraokeapp.view.custom_view

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import java.lang.Runnable as Runnable
import android.view.MotionEvent
import android.widget.Chronometer
import com.example.androidkaraokeapp.model.LyricModel

class LyricTextView: TextView {
    private var mHeight = 0
    private var mWidth = 0
    private var fontSize = 30f
    private var lyric = "Textview"
    private var paint = Paint()
    private var isInit = false
    private var duration:Float = 500F
    private var currentMilis:Float = 0F
    private var strokeWidth = 10f
    private lateinit var chronometer: Chronometer

    var framePerSecond:Long = 1000/24
    var isRunning = false


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun initView () {
        fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,fontSize, resources.displayMetrics)
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textSize = fontSize
        paint.textAlign = Paint.Align.CENTER
        paint.strokeWidth = strokeWidth
        mHeight = height
        isInit = true
        chronometer = Chronometer(context)
    }



    //region override method
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(mWidth,MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(150,MeasureSpec.EXACTLY))
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if ( !isInit) {
            initView()
        }
        drawText(canvas)
    }


    //endregion

    //region private method

    private fun drawText(canvas: Canvas?) {

            val bounds = Rect()
            paint.getTextBounds(lyric, 0, lyric.length, bounds)

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

                paint.shader = shader
                canvas.drawText(lyric, 0, lyric.length , canvas.width/2.toFloat(), canvas.height/1.5.toFloat(), paint)

                if ( isRunning ){
                    currentMilis = (SystemClock.elapsedRealtime() - chronometer.base).toFloat()
                    postInvalidateDelayed(framePerSecond)
                }
            }
            else {
                isRunning = false
                chronometer.stop()
             }
    }

    fun setKaraokeLyric(mLyric: LyricModel,currentPlayPosition:Int, mediaPlayerIsPlaying: Boolean) {
        lyric = mLyric.text

        duration = mLyric.to - currentPlayPosition
        currentMilis =0F

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        mWidth =  paint.measureText(lyric, 0, lyric.length).toInt()
        isRunning = mediaPlayerIsPlaying

        requestLayout()

    }
}