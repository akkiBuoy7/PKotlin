package plm.patientslikeme2.utils.usercontrol

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import plm.patientslikeme2.R
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.enum.Theme

class SwitchTrackTextDrawable(
    private val mContext: Context,
    @StringRes leftTextId: Int,
    @StringRes rightTextId: Int,
) : Drawable() {
    private val mLeftText: String = mContext.getString(leftTextId)
    private val mRightText: String
    private val mTextPaint: Paint
    private fun createTextPaint(): Paint {

        val appThemeMode: Theme = getByValue(
            Preferences.getValue(Constants.APP_THEME, Theme.System.ordinal)
        )
        val textPaint = Paint()
        if (appThemeMode.toString() == "Dark") {
            textPaint.color = mContext.resources.getColor(android.R.color.white)
        } else {
            textPaint.color = mContext.resources.getColor(R.color.black)
        }
        textPaint.isAntiAlias = true
        textPaint.style = Paint.Style.FILL
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 38f
        // Set textSize, typeface, etc, as you wish
        return textPaint
    }

    fun getByValue(value: Int): Theme {
        val values = Theme.values()
        return values.firstOrNull { it.ordinal == value } ?: Theme.System
    }

    override fun draw(canvas: Canvas) {
        val textBounds = Rect()
        mTextPaint.getTextBounds(mRightText, 0, mRightText.length, textBounds)

        // The baseline for the text: centered, including the height of the text itself
        val heightBaseline = canvas.clipBounds.height() / 2 + textBounds.height() / 2

        // This is one quarter of the full width, to measure the centers of the texts
        val widthQuarter = canvas.clipBounds.width() / 4
        canvas.drawText(
            mLeftText,
            0,
            mLeftText.length,
            widthQuarter.toFloat(),
            heightBaseline.toFloat(),
            mTextPaint
        )
        canvas.drawText(
            mRightText,
            0,
            mRightText.length,
            (widthQuarter * 3).toFloat(),
            heightBaseline.toFloat(),
            mTextPaint
        )
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    init {

        // Left text
        mTextPaint = createTextPaint()

        // Right text
        mRightText = mContext.getString(rightTextId)

    }
}