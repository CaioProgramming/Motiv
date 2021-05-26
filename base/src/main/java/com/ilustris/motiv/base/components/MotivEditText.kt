package com.ilustris.motiv.base.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.ilustris.motiv.base.R
import java.lang.Exception

/**
 * TODO: document your custom view class.
 */
class MotivEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyle: Int = 0, defStyleAttr: Int
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var isRtl: Boolean = false

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
        context.theme.obtainStyledAttributes(attrs, R.styleable.MotivEditText, 0, 0).apply {
            try {
                strokeWidth = getFloat(R.styleable.MotivEditText_motivEditStrokeWidth, 1f)
                strokeColor = getColor(R.styleable.MotivEditText_motivEditStrokeColor, Color.WHITE)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                recycle()
            }
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // save the original text color since TextPaint will change to draw shadow and stroke
        val originalTextColor = currentTextColor
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