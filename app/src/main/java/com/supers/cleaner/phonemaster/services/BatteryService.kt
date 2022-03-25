package com.supers.cleaner.phonemaster.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BatteryService: Service() {
    override fun onBind(p0: Intent?): IBinder? {

        return  null
    }

    override fun onCreate() {
        super.onCreate()
        val   mBatteryStatusReceiver = BatterySaverReceiver()
        mBatteryStatusReceiver.OnCreate(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)

    }
}