package com.supers.cleaner.phonemaster.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.service.notification.StatusBarNotification
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.ui.MainActivity


class BatterySaverReceiver: BroadcastReceiver() {
lateinit var mContext :Context
var i = 0
    override fun onReceive(context: Context, p1: Intent?) {
        mContext = context
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationLayout = RemoteViews("com.supers.cleaner.phonemaster", R.layout.battery_notification_layout)
        val notificationIntent = Intent(context,MainActivity::class.java)
        notificationIntent.putExtra("whattodo","energysaving")
        val pendingIntent = PendingIntent.getActivity(context,0, notificationIntent, PendingIntent.FLAG_MUTABLE)
i++
        notificationLayout.setOnClickPendingIntent(R.id.ll_batterysaver, pendingIntent)
        val customNotification = NotificationCompat.Builder(context, "battery")
            .setSmallIcon(R.drawable.ic_clean_ram)
            .setCustomContentView(notificationLayout)
            .setAutoCancel(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "battery",
                context.resources.getString(R.string.app_name)+"battery",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        val level = p1!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = p1.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val status: Int = p1.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL


       customNotification.flags = customNotification.flags or Notification.FLAG_AUTO_CANCEL
        if(level.toFloat() / scale.toFloat() * 100.0f==30.0f||level.toFloat() / scale.toFloat() * 100.0f==20.0f||level.toFloat() / scale.toFloat() * 100.0f==75.0f) {

if(!isCharging) {
    notificationManager.notify(2, customNotification)

}
            else{
                notificationManager.cancel(2)
            }
        }
        }

    private fun isNotificationVisible(context: Context, intent: Intent): Boolean {
        val test = PendingIntent.getActivity(
            context,
            2,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        return test != null
    }
    fun OnCreate(context: Context) {
        mContext = context.applicationContext
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
//        intentFilter.addAction(BatteryService.ACTION_MAX_BATTERY_NEED_UPDATE)
//        intentFilter.addAction(NotificationBattery.UPDATE_NOTIFICATION_ENABLE)

        mContext.registerReceiver(this, intentFilter)
    }

}