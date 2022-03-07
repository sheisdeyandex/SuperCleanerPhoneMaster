package com.supers.cleaner.phonemaster.Utils

import android.content.Context
import com.supers.cleaner.phonemaster.MyApplication

object PreferencesProvider{

    private val preferences = MyApplication.getInstance().getSharedPreferences("waseem", Context.MODE_PRIVATE)

    fun getInstance() = preferences
}