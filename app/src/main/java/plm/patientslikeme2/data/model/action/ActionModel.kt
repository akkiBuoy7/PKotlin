package plm.patientslikeme2.data.model.action

import android.graphics.drawable.Drawable

data class ActionModel(
    var isSuccess: Boolean,
    var drawable: Drawable,
    var message: String
)