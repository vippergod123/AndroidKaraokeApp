package com.example.androidkaraokeapp.ulti

import android.graphics.*
import com.squareup.picasso.Transformation
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.RectF
import android.graphics.Bitmap
import android.graphics.Shader
import android.graphics.BitmapShader


class RoundedTransformation(private var radius: Float, private var margin: Float) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(
            source, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )

        val output = Bitmap.createBitmap(
            source.width, source.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        canvas.drawRoundRect(
            RectF(
                margin, margin, (source.width - margin),
                (source.height - margin)
            ), radius, radius, paint
        )

        if (source != output) {
            source.recycle()
        }
        return output
    }

    override fun key(): String {
        return "rounded(r=$radius, m=$margin)"
    }
}


