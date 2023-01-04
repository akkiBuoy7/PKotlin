package plm.patientslikeme2.utils.usercontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import plm.patientslikeme2.R
import kotlin.math.cos
import kotlin.math.sin

class HexagonImage : AppCompatImageView {
    private val polygonFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePath = Path()
    private var strokeWidth = 4f

    var numberOfSides = 6
        set(numberOfSides) {
            field = numberOfSides
            invalidate()
        }

    var cornerRadius = 10f
        set(cornerRadius) {
            field = cornerRadius
            invalidate()
        }

    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {

        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.HexagonImage)
            polygonFillPaint.color = attributes.getInt(R.styleable.HexagonImage_hexagonColor, 0)
        }

        invalidate()
    }

    fun setColor(color: Int) {
        polygonFillPaint.color = color
        invalidate()
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()
        val radius = (width / 2 - strokeWidth)

        val halfInteriorCornerAngle: Double = 90 - 180.0 / numberOfSides
        val halfCornerArcSweepAngle = (90 - halfInteriorCornerAngle).toFloat()
        val distanceToCornerArcCenter: Double =
            radius - cornerRadius / sin(toRadians(halfInteriorCornerAngle))

        val tempCornerArcBounds = RectF()

        strokePath.reset()
        for (cornerNumber in 0 until numberOfSides) {
            val angleToCorner: Double = cornerNumber * (360.0 / numberOfSides)
            val cornerCenterX =
                (centerX + distanceToCornerArcCenter * cos(toRadians(angleToCorner))).toFloat()
            val cornerCenterY =
                (centerY + distanceToCornerArcCenter * sin(toRadians(angleToCorner))).toFloat()
            tempCornerArcBounds.set(
                cornerCenterX - cornerRadius,
                cornerCenterY - cornerRadius,
                cornerCenterX + cornerRadius,
                cornerCenterY + cornerRadius
            )

            strokePath.arcTo(
                tempCornerArcBounds,
                (angleToCorner - halfCornerArcSweepAngle).toFloat(),
                2 * halfCornerArcSweepAngle
            )
        }

        strokePath.close()
        canvas.drawPath(strokePath, polygonFillPaint)
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
        invalidate()
    }

    private fun toRadians(degrees: Double): Double {
        return 2 * Math.PI * degrees / 360
    }
}