package br.digitalhouse.cookbook.data.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.ui.dashboard.view.DashBoardActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService: FirebaseMessagingService() {

    private val TAG = "notifica"
    private var intent: Intent? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //log para ver os dados que estao vindo
        Log.i(TAG, "onMessageReceived: " + remoteMessage.data)

        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["titulo"]
            val body = remoteMessage.data["descricao"]
            val type = remoteMessage.data["type"]

            setMessage(title, body, type)
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.i(TAG, "onNewToken: $s")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setMessage(title: String?, body: String?, type: String?) {

        val channel = getString(R.string.default_notification_channel_id)
        val uriSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        intent = Intent(this, DashBoardActivity::class.java)


        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(this, channel)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(uriSound)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        val nChannel =
            NotificationChannel(channel, "channel", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(nChannel)
        notificationManager.notify(0, notification.build())

    }
}