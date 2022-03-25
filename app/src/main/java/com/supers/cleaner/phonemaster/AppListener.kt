//package com.supers.cleaner.phonemaster
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.TaskStackBuilder
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.media.AudioAttributes
//import android.media.RingtoneManager
//import android.net.Uri
//import android.os.Build
//import android.util.Log
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.ImageView
//import android.widget.RemoteViews
//import android.widget.TextView
//import android.widget.Toast
//import androidx.core.app.NotificationCompat
//import com.supers.cleaner.phonemaster.ui.MainActivity
//
///**
// * Created by intag pc on 3/3/2017.
// */
//
//class AppListener : BroadcastReceiver() {
//
//
//    /// Listen for newly installed app and protect it
//
//    override fun onReceive(context: Context, arg1: Intent) {
//        // TODO Auto-generated method stub
//
//        val data = arg1.data
//        val installedPackageName = data!!.encodedSchemeSpecificPart
//Log.d("sukawtfhappend", installedPackageName)
//        val notificationLayout = RemoteViews("com.supers.cleaner.phonemaster", R.layout.appinstalled_notification_layout)
//
//
//        val notificationIntent = Intent(context, MainActivity::class.java)
//        notificationIntent.putExtra("whattodo", "optimize")
//        val stackBuilder = TaskStackBuilder.create(context)
//        stackBuilder.addNextIntent(notificationIntent)
//        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//        notificationLayout.setOnClickPendingIntent(R.id.ll_optimize, pendingIntent)
//        val customNotification = NotificationCompat.Builder(context, AlarmReceiver.CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setCustomContentView(notificationLayout)
//            .build()
//        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                AlarmReceiver.CHANNEL_ID,
//                context.resources.getString(R.string.app_name),
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val audioAttributes = AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                .build()
//            channel.setSound(alarmSound, audioAttributes)
//            notificationManager.createNotificationChannel(channel)
//            notificationManager.notify(1, customNotification)
//        }
//
//        if (installedPackageName != "com.supers.cleaner.phonemaster") {
//            //            Toast.makeText(context, installedPackageName + "", Toast.LENGTH_SHORT).show();
//
//            val packageManager = context.applicationContext.packageManager
//            try {
//                val appName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(installedPackageName, PackageManager.GET_META_DATA)) as String
//                val inflater = LayoutInflater.from(context)
//                val layout = inflater.inflate(R.layout.my_toast, null)
//
//                val text = layout.findViewById<View>(R.id.textView1) as TextView
//                text.text = appName + context.getString(R.string.is_optimised_by)
//
//                val toast = Toast(context)
//                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 120)
//                toast.duration = Toast.LENGTH_LONG
//                toast.view = layout
//                toast.show()
//            } catch (e: PackageManager.NameNotFoundException) {
//                e.printStackTrace()
//            }
//
//        }
//    }
//}
