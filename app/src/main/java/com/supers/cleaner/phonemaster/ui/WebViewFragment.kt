package com.supers.cleaner.phonemaster.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentCleanBinding
import com.supers.cleaner.phonemaster.databinding.FragmentWebViewBinding
import com.supers.cleaner.phonemaster.interfaces.IFragment

class WebViewFragment(iFragment:IFragment) : Fragment() {
    val iFragment:IFragment =iFragment
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.mbtnCancel.setOnClickListener{
            iFragment.regulate(true,7)
            MyApplication.showuserpolicy = false
        }
        binding.mbtnAccept.setOnClickListener{
            iFragment.regulate(true,7)
            MyApplication.showuserpolicy = false
        }
        binding.webviewPolicy.settings.domStorageEnabled = true
        binding.webviewPolicy.settings.javaScriptEnabled = true
        binding.webviewPolicy.webViewClient = object :WebViewClient(){}
        binding.webviewPolicy.webChromeClient = object :WebChromeClient(){}
        binding.webviewPolicy.settings.setSupportMultipleWindows(true)
        binding.webviewPolicy.settings.allowFileAccess = true
        binding.webviewPolicy.loadUrl("https://sites.google.com/view/super-cleaner-phone-master/главная-страница")
        return view
    }

}