package com.ilustris.motiv.base.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setPadding
import com.ilustris.motiv.base.R
import com.ilustris.motiv.base.beans.ShadowStyle
import java.lang.Exception

/**
 * TODO: document your custom view class.
 */
class ExtrudeTextView
    : AppCompatTextView {

    private var isRtl: Boolean = false

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
            this@ExtrudeTextView.strokeColor = Color.parseColor(strokeColor)
        }
    }

    private fun refreshLayout() {
        invalidate()
        requestLayout()
    }

    fun initView(attrs: AttributeSet? = null, defStyle: Int) {
        attrs?.let {
            context.theme.obtainStyledAttributes(attrs, R.styleable.ExtrudeTextView, 0, 0).apply {
                try {
                    addExtraPadding()

                    extrudeDepth = getInt(R.styleable.ExtrudeTextView_extrudeDepth, 1)
                    extrudeColor = getColor(R.styleable.ExtrudeTextView_extrudeColor, Color.BLACK)
                    strokeWidth = getFloat(R.styleable.ExtrudeTextView_extrudeStrokeWidth, 1f)
                    strokeColor =
                        getColor(R.styleable.ExtrudeTextView_extrudeStrokeColor, Color.WHITE)
                    xPosition = getFloat(R.styleable.ExtrudeTextView_extrudeXPosition, 1f)
                    yPosition = getFloat(R.styleable.ExtrudeTextView_extrudeYPosition, 1f)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    recycle()
                }
            }
        }
    }

    private fun addExtraPadding() {
        setPadding(0)
        setPadding(16)
        setPadding(
            paddingStart,
            paddingTop + extrudeDepth,
            paddingEnd + extrudeDepth,
            paddingBottom
        )
    }


    override fun onDraw(canvas: Canvas?) {
        val originalTextColor = currentTextColor
        canvas?.save()
        if (extrudeDepth > 0) {// save the original text color since TextPaint will change to draw shadow and stroke
            paint.color = extrudeColor

            canvas?.save()

            // take into account padding set on the view
            val translateX = if (isRtl) paddingEnd else paddingStart
            canvas?.translate(translateX.toFloat(), paddingTop.toFloat())

            // change extrude direction based on RTL
            val dx = xPosition
            val dy = xPosition

            // draw same text multiple times with shifting x and y position
            for (i in -1..extrudeDepth) {
                canvas?.translate(dx, dy)
                layout.draw(canvas)
            }

            // draw text in original color
            paint.color = originalTextColor
            layout.draw(canvas)

            // add stroke around text
            paint.color = strokeColor
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
            layout.draw(canvas)

            // revert paint to original state
        }
        paint.color = originalTextColor
        paint.style = Paint.Style.FILL
        layout.draw(canvas)

    }
}