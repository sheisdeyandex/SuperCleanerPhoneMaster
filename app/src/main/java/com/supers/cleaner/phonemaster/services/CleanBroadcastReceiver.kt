package com.supers.cleaner.phonemaster.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.supers.cleaner.phonemaster.AlarmReceiver.Companion.CHANNEL_ID
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.ui.MainActivity

class CleanBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        //
   val notificationLayout = RemoteViews("com.supers.cleaner.phonemaster", R.layout.clean_notification_layout)
//

    val notificationIntent = Intent(context, MainActivity::class.java)
notificationIntent.putExtra("whattodo", "optimize")
       val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(notificationIntent)

//
       val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_MUTABLE)
//
        notificationLayout.setOnClickPendingIntent(R.id.btn_clean, pendingIntent)
// Apply the layouts to the notification
        val customNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setCustomContentView(notificationLayout)
            .setAutoCancel(true)
            .build()
//       notificationLayout.setOnClickPendingIntent(R.id.ll_boost, intent.)

//        if (MyOnClick == intent.action){
//            //your onClick action is here
//        }
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

//        val notification = builder.setContentTitle(PreferencesProvider.getInstance().getString("state_Head", "Error"))
//                .setContentText(PreferencesProvider.getInstance().getString("state_Body", "Increase device performance"))
//                .setAutoCancel(true)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setDefaults(Notification.DEFAULT_SOUND)
//                .setContentIntent(pendingIntent).addAction(R.drawable.ic_launcher_background,"press",
//                pendingIntent).build()


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder.setChannelId(CHANNEL_ID)
//        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    context.resources.getString(R.string.app_name),
                    IMPORTANCE_DEFAULT
            )
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            channel.setSound(alarmSound, audioAttributes)
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(1, customNotification)
        }
    }
}