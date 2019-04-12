package com.example.androidkaraokeapp.view.custom_view

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import java.lang.Runnable as Runnable
import android.opengl.ETC1.getHeight
import android.view.MotionEvent


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
    private var framePerSecond:Long = 1000/24

    var isRunning = false

    constructor(context: Context) : this(context, null) {
//        if ( !isInit) {
//            initView()
//        }
        println()

    }
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)



    private fun initView () {

        fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,fontSize, resources.displayMetrics)
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textSize = fontSize
        paint.textAlign = Paint.Align.CENTER
        paint.strokeWidth = strokeWidth
        mHeight = height
//        mWidth =  paint.measureText(lyric, 0, lyric.length).toInt()
        isInit = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mWidth =  paint.measureText(lyric, 0, lyric.length).toInt()
        super.onMeasure(MeasureSpec.makeMeasureSpec(paint.measureText(lyric, 0, lyric.length).toInt(),MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(150,MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if ( !isInit) {
            initView()

        }
        drawText(canvas)
    }

    //region private method

    private fun drawText(canvas: Canvas?) {
        if (isRunning) {

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
//                canvas.drawText(lyric, 0f, height.toFloat(), paint)
//                                canvas?.drawText(lyric, 0f, height.toFloat(), paint)
                canvas.drawText(lyric, 0, lyric.length , canvas.width/2.toFloat(), canvas.height/1.5.toFloat(), paint)
                currentMilis += framePerSecond
                postInvalidateDelayed(framePerSecond)
            } else {
                isRunning = false
             }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
    fun setTextAndDuration(mLyric: String, mDuration:Float) {
        lyric = mLyric
        duration = mDuration - 1000f
        currentMilis =0F
        mWidth =  paint.measureText(lyric, 0, lyric.length).toInt()
        isRunning = true
        requestLayout()
        invalidate()

    }
}