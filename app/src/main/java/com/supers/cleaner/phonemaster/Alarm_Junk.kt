package com.supers.cleaner.phonemaster

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.supers.cleaner.phonemaster.Utils.PreferencesProvider

/**
 * Created by intag pc on 3/2/2017.
 */

class Alarm_Junk : BroadcastReceiver() {

    //todo Prefs

    companion object {
        private var needToCheck = MutableLiveData<Boolean>()
        fun getNeedToCheck() = needToCheck
    }

    override fun onReceive(context: Context, intent: Intent) {
        PreferencesProvider.getInstance().edit().putString("junk", "1").apply()

        try {
            needToCheck.postValue(true)
        } catch (e: Exception) {

        }

    }

}

