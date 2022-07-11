package com.supers.cleaner.phonemaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentCleanBinding
import com.supers.cleaner.phonemaster.databinding.FragmentFraNoAdsBinding

class FraFragmentNoAds : Fragment() {
    private var _binding: FragmentFraNoAdsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFraNoAdsBinding.inflate(inflater, container, false)
        val view = binding.root
binding.clApps.setOnClickListener {
    (requireActivity() as MainActivity).selectTab("appsmanager")
    (requireActivity() as MainActivity).binding.bnvNav.visibility = View.GONE
}
        binding.mcvPremium.setOnClickListener {
            (requireActivity() as MainActivity).selectTab("premium")
        }
        return view
    }


}