package plm.patientslikeme2.utils.extensions

import android.text.Html
import plm.patientslikeme2.utils.usercontrol.UserEditText

fun UserEditText.setHtmlText(string: String?) {
    this.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT))
}