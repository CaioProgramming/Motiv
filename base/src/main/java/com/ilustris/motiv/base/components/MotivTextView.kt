package com.ilustris.motiv.base.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.beans.ShadowStyle
import java.lang.Exception

/**
 * TODO: document your custom view class.
 */
class MotivTextView
    : AppCompatTextView {


    constructor(context: Context) : super(context) {
        initView(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView(attrs, defStyle)
    }

    var extrudeColor: Int = Color.BLACK
        set(value) {
            field = value
            refreshLayout()
        }


    var extrudeDepth: Int = 0
        set(value) {
            field = value
            //addExtraPadding()
            refreshLayout()
        }

    var strokeWidth: Float = 1f
        set(value) {
            field = value
            refreshLayout()
        }

    var strokeColor: Int = Color.WHITE
        set(value) {
            field = value
            refreshLayout()
        }

    var xPosition: Float = 1f
        set(value) {
            field = value
            refreshLayout()
        }

    var yPosition: Float = 0f
        set(value) {
            field = value
            refreshLayout()
        }

    fun setExtrude(shadowStyle: ShadowStyle) {
        shadowStyle.run {
            xPosition = if (xPosition > 10) 10 / xPosition else dx
            yPosition = if (yPosition > 10) 10 / xPosition else dy
            extrudeDepth = if (radius > 5) 5 / radius.toInt() else radius.toInt()
            extrudeColor = Color.parseColor(shadowColor)
            this@MotivTextView.strokeColor = Color.parseColor(strokeColor)
        }
    }


    private fun refreshLayout() {
        invalidate()
        requestLayout()
    }

    fun initView(attrs: AttributeSet? = null, defStyle: Int) {
        attrs?.let {
            context.theme.obtainStyledAttributes(attrs, R.styleable.MotivTextView, 0, 0).apply {
                try {
                    strokeWidth = getDimension(R.styleable.MotivTextView_motivStrokeWidth, 1f)
                    strokeColor = getColor(R.styleable.MotivTextView_motivStrokeColor, Color.BLACK)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    recycle()
                }
            }
        }
    }



    override fun onDraw(canvas: Canvas?) {
        val originalTextColor = currentTextColor
        canvas?.save()
        // add stroke around text
        paint.color = strokeColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        canvas?.let {
            layout.draw(it)
        }
        paint.color = originalTextColor
        paint.style = Paint.Style.FILL
        canvas?.let {
            layout.draw(it)
        }
    }
}