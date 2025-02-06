package us.smt.mylearningcentre.service.pushnotification

//import us.smt.mylearningcentre.data.repository.AuthRepositoryImpl
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import us.smt.mylearningcentre.MainActivity
import us.smt.mylearningcentre.R
import us.smt.mylearningcentre.data.database.remote.FireBaseHelper

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        val title = remoteMessage.notification?.title ?: "Yangi xabar"
        val body = remoteMessage.notification?.body ?: "Xabar tafsilotlari yo‘q"

        showNotification(title, body)
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "FCM_CHANNEL"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Firebase Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun sendTokenToServer(token: String) {
//        localStorage.fcmToken = token
        val firebase = FireBaseHelper.getInstance()
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                firebase.updateItem(
//                    localStorage.userId,
//                    mapOf(AuthRepositoryImpl.fcmToken to token),
//                    FireBaseHelper.collectionStudent
//                )
//            } catch (_: Exception) {
//            }
//        }
    }

}
