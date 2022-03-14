package com.supers.cleaner.phonemaster.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.supers.cleaner.phonemaster.AlarmReceiver
import com.supers.cleaner.phonemaster.R

class WidgetService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return  null
    }

    override fun onCreate() {
        super.onCreate()
        setCustomNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    private fun setCustomNotification(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationLayout = RemoteViews("com.supers.cleaner.phonemaster", R.layout.custom_notification_layout)
//    val notificationIntent = Intent()
//  //  intent.putExtra("whattodo","boost")
//    val stackBuilder = TaskStackBuilder.create(applicationContext)
//stackBuilder.addNextIntent(notificationIntent)
////
//    val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//////
//    notificationLayout.setOnClickPendingIntent(R.id.ll_boost, pendingIntent)
        val customNotification = NotificationCompat.Builder(applicationContext, AlarmReceiver.CHANNEL_ID)
            .setSmallIcon(android.R.color.transparent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setOngoing(true)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AlarmReceiver.CHANNEL_ID,
                resources.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        startForeground(0, customNotification)

    }
}