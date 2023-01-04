package plm.patientslikeme2.utils.usercontrol

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.text.TextPaint
import plm.patientslikeme2.R
import java.util.*

class AvatarGenerator {
    companion object {
        lateinit var uiContext: Context
        var texSize = 0F

        fun avatarImage(
            context: Context,
            size: Int,
            shape: Int,
            name: String,
            color: Int
        ): BitmapDrawable {
            uiContext = context

            texSize = calTextSize(size)
            val label = firstCharacter(name)
            val textPaint = textPainter()
            val painter = painter()
            val areaRect = Rect(0, 0, size, size)

            if (color != 0) {
                painter.color = color
            } else {
                if (shape == 0) {
                    painter.color = context.getColor(R.color.teal)
                } else {
                    painter.color = Color.TRANSPARENT
                }
            }

            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawRect(areaRect, painter)

            //reset painter
            if (color != 0) {
                painter.color = color
            } else {
                if (shape == 0) {
                    painter.color = Color.TRANSPARENT
                } else {
                    //painter.color = RandomColors().getColor()
                    // using same colour for profile avatar for time being
                    painter.color = context.getColor(R.color.teal)
                }
            }

            val bounds = RectF(areaRect)
            bounds.right = textPaint.measureText(label, 0, 1)
            bounds.bottom = textPaint.descent() - textPaint.ascent()

            bounds.left += (areaRect.width() - bounds.right) / 2.1f
            bounds.top += (areaRect.height() - bounds.bottom) / 2.1f

            canvas.drawCircle(
                size.toFloat() / 2.1f,
                size.toFloat() / 2.1f,
                size.toFloat() / 2.1f,
                painter
            )

            //val textPaint = Paint()
            textPaint.textAlign = Paint.Align.CENTER
            val fontStyle = Typeface.createFromAsset(context.assets, "fonts/Poppins-Regular.ttf")
            textPaint.typeface = fontStyle;

            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.
            //canvas.drawText(label.trim(), xPos, yPos, textPaint)

            canvas.getClipBounds(areaRect)
            val cHeight: Int = areaRect.height()
            val cWidth: Int = areaRect.width()
            textPaint.textAlign = Paint.Align.LEFT
            textPaint.getTextBounds(label.trim(), 0, label.trim().length, areaRect)
            val x: Float = cWidth / 2f - areaRect.width() / 2f - areaRect.left
            val y: Float = cHeight / 2f + areaRect.height() / 2f - areaRect.bottom
            canvas.drawText(label.trim(), x, y, textPaint)

            return BitmapDrawable(uiContext.resources, bitmap)
        }

        private fun firstCharacter(name: String): String {
            return name.first().toString().uppercase(Locale.getDefault())
        }

        private fun textPainter(): TextPaint {
            val textPaint = TextPaint()
            textPaint.textSize = texSize * uiContext.resources.displayMetrics.scaledDensity
            textPaint.color = Color.WHITE
            return textPaint
        }

        private fun painter(): Paint {
            return Paint()
        }

        private fun calTextSize(size: Int): Float {
            return (size / 6).toFloat()
        }
    }
}