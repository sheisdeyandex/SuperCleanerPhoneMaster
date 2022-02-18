package com.supers.cleaner.phonemaster.ui
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentOptimizeBinding
import com.supers.cleaner.phonemaster.databinding.FragmentSplashScreenBinding
import android.content.Intent
import android.net.Uri


class SplashScreen : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.tvPrivacy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/super-cleaner-phone-master/главная-страница"))
            startActivity(browserIntent)
        }
        return view
    }


}