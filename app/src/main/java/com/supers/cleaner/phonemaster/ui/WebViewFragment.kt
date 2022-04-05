package com.supers.cleaner.phonemaster.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.ActivityMainBinding
import com.supers.cleaner.phonemaster.databinding.FragmentCleanBinding
import com.supers.cleaner.phonemaster.databinding.FragmentWebViewBinding
import com.supers.cleaner.phonemaster.interfaces.IFragment

class WebViewFragment : AppCompatActivity() {
    lateinit var binding:FragmentWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mbtnCancel.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
        binding.mbtnAccept.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
        binding.webviewPolicy.settings.domStorageEnabled = true
        binding.webviewPolicy.settings.javaScriptEnabled = true
        binding.webviewPolicy.webViewClient = object :WebViewClient(){}
        binding.webviewPolicy.webChromeClient = object :WebChromeClient(){}
        binding.webviewPolicy.settings.setSupportMultipleWindows(true)
        binding.webviewPolicy.settings.allowFileAccess = true
        binding.webviewPolicy.loadUrl("https://sites.google.com/view/super-cleaner-phone-master/главная-страница")

    }



}