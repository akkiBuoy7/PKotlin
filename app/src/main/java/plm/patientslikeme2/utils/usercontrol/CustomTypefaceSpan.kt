package plm.patientslikeme2.utils.usercontrol

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class CustomTypefaceSpan(
    family: String?,
    private val newType: Typeface,
    val color: Int,
) : TypefaceSpan(family) {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType, color)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType, color)
    }

    companion object {
        private fun applyCustomTypeFace(paint: Paint, tf: Typeface, color: Int) {
            val oldStyle: Int
            val old: Typeface = paint.typeface
            oldStyle = old.style
            val fake = oldStyle and tf.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.isFakeBoldText = true
            }
            if (fake and Typeface.ITALIC != 0) {
                paint.textSkewX = -0.25f
            }
            paint.color = color
            paint.typeface = tf
        }
    }
}