package plm.patientslikeme2.utils.extensions

import android.content.Intent
import android.net.Uri
import plm.patientslikeme2.utils.usercontrol.UserButton

fun UserButton.openHyperLinks(url: String?) {
    url?.let {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        this.context.startActivity(intent)
    }
}