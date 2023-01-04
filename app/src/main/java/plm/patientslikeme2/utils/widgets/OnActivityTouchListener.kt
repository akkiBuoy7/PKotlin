package plm.patientslikeme2.utils.widgets

import android.view.MotionEvent

interface OnActivityTouchListener {
    fun getTouchCoordinates(ev: MotionEvent?)
}