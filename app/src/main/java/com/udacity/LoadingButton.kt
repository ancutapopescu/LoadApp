package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.renderscript.Sampler
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    // Paint object for coloring and styling Button
    private var paintButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorPrimary)
        isAntiAlias = true
    }

    private var paintLoadingButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorPrimaryDark)
        isAntiAlias = true
    }

    private lateinit var buttonText: String

    // Paint object for coloring and styling Text on the Button
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.white)
        isAntiAlias = true
        textSize = 40.0f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)

    }

    // Paint object for coloring and styling Circle
    private var paintCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorAccent)
        isAntiAlias = true
    }


    private var valueAnimator = ValueAnimator()

    var value = 0.0f
    var width = 0.0f
    var sweepAngle = 0.0f

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        // Set the button text depending on the state of the button
        buttonText = context.getString(buttonState.customTextButton)

        when(new) {

            ButtonState.Loading -> {
               paintCircle.color = context.getColor(R.color.colorAccent)
               valueAnimator = ValueAnimator.ofFloat(0.0f, measuredWidth.toFloat()).setDuration(2000).apply {
                    addUpdateListener { valueAnimator ->
                        value = valueAnimator.animatedValue as Float
                        sweepAngle = value / 8
                        width = value / 5
                        invalidate()
                    }
                }
                valueAnimator.start()
            }

            ButtonState.Completed -> {
                valueAnimator.cancel()
                value = 0.0f
                invalidate()
            }
        }
    }


    init {
        buttonState = ButtonState.Clicked
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0.0f, 0.0f, widthSize.toFloat(), heightSize.toFloat(), paintButton)
        canvas?.drawRect(0f, 0f, width, heightSize.toFloat(), paintLoadingButton)

        canvas?.drawText(buttonText, widthSize.toFloat() / 2.0f, heightSize.toFloat() / 2.0f, paintText)

        canvas?.drawArc(widthSize - 145f,
                heightSize / 2 - 35f,
                widthSize - 75f,
                heightSize / 2 + 35f,
                0F,
                width,
                true,
                paintCircle)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}