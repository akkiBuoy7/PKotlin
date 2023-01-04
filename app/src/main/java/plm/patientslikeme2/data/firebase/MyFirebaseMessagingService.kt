package plm.patientslikeme2.data.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import plm.patientslikeme2.R
import plm.patientslikeme2.ui.main.view.activity.home.MainActivity
import plm.patientslikeme2.utils.Constants.CONTEXT_ID
import plm.patientslikeme2.utils.Constants.CONTEXT_TYPE
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(CONTEXT_ID, remoteMessage.data[CONTEXT_ID])
            intent.putExtra(CONTEXT_TYPE, remoteMessage.data[CONTEXT_TYPE])
            showNotification(this, it.title, it.body, intent)
        }
    }

    private fun showNotification(
        context: Context,
        title: String?,
        message: String?,
        intent: Intent?
    ) {
        val min = 1
        val max = 100
        val random = Random().nextInt(max - min + 1) + min

        val pendingIntent =
            PendingIntent.getActivity(context, random, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "PLM_MAIN"
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getContextText(title, message))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name: CharSequence = "PLM"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(channelId, name, importance)
        notificationManager.createNotificationChannel(mChannel)
        notificationManager.notify(random, notificationBuilder.build())
    }

    private fun getContextText(title: String?, body: String?): String {
        return if (!title.isNullOrEmpty() && !body.isNullOrEmpty()) {
            "$title $body"
        } else {
            body.toString()
        }
    }

    override fun onNewToken(token: String) {
        Log.d("FCM New Token Is - ", token)
    }
}