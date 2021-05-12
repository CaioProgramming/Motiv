package com.ilustris.motiv.base.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.ilustris.motiv.base.R
import java.lang.Exception

/**
 * TODO: document your custom view class.
 */
class ExtrudeEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyle: Int = 0, defStyleAttr: Int
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var isRtl: Boolean = false

    var extrudeColor: Int = Color.BLACK
        set(value) {
            field = value
            refreshLayout()
        }


    var extrudeDepth: Int = 0
        set(value) {
            field = value
            addExtraPadding()
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


    private fun refreshLayout() {
        invalidate()
        requestLayout()
    }

    init {
        addExtraPadding()
        context.theme.obtainStyledAttributes(attrs, R.styleable.ExtrudeEditText, 0, 0).apply {
            try {
                extrudeDepth = getInt(R.styleable.ExtrudeEditText_editExtrudeDepth, 1)
                extrudeColor = getColor(R.styleable.ExtrudeEditText_editExtrudeColor, Color.BLACK)
                strokeWidth = getFloat(R.styleable.ExtrudeEditText_editExtrudeStrokeWidth, 1f)
                strokeColor =
                    getColor(R.styleable.ExtrudeEditText_editExtrudeStrokeColor, Color.WHITE)
                xPosition = getFloat(R.styleable.ExtrudeEditText_editExtrudeXPosition, 1f)
                yPosition = getFloat(R.styleable.ExtrudeEditText_editExtrudeYPosition, 1f)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                recycle()
            }
        }
    }


    private fun addExtraPadding() {
        val config = context.resources.configuration
        isRtl = config.layoutDirection == View.LAYOUT_DIRECTION_RTL
        if (isRtl) {
            // in Right to left the start is on the right
            setPadding(
                paddingEnd + extrudeDepth,
                paddingTop + extrudeDepth,
                paddingStart,
                paddingBottom
            )
        } else {
            setPadding(
                paddingStart,
                paddingTop + extrudeDepth,
                paddingEnd + extrudeDepth,
                paddingBottom
            )
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // save the original text color since TextPaint will change to draw shadow and stroke
        val originalTextColor = currentTextColor
        paint.color = extrudeColor

        canvas?.save()

        // take into account padding set on the view
        val translateX = if (isRtl) paddingEnd else paddingStart
        canvas?.translate(translateX.toFloat(), paddingTop.toFloat())

        // change extrude direction based on RTL
        val dx = xPosition
        val dy = yPosition

        // draw same text multiple times with shifting x and y position
        for (i in 0..extrudeDepth) {
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
        paint.color = originalTextColor
        paint.style = Paint.Style.FILL
        canvas?.restore()
    }
}