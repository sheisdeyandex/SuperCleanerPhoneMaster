package com.supers.cleaner.phonemaster.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentEnergySavingBinding
import com.supers.cleaner.phonemaster.databinding.FragmentUltraPowerSavingBinding
import com.supers.cleaner.phonemaster.interfaces.IBanner
import com.supers.cleaner.phonemaster.interfaces.IFragment

class FragmentUltraPowerSaving(ultrapowerText:String, iFragment: IFragment, iBanner: IBanner) : Fragment() {

    val ultrapowerText:String
    val iFragment:IFragment
    val iBanner:IBanner = iBanner
    init {
        this.iFragment = iFragment
        this.ultrapowerText = ultrapowerText
    }
    private var _binding: FragmentUltraPowerSavingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUltraPowerSavingBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.tvUltrapowertext.text = binding.tvUltrapowertext.text.toString() +" "+ ultrapowerText
        binding.materialButtonDoIt.setOnClickListener {
            MyApplication.worktime =  ultrapowerText
            iFragment.regulate(false, 4)
        }
        var   adRequest = AdRequest.Builder().build()
        if(!MyApplication.premiumUser){
            binding.avBanner.loadAd(adRequest)
        }
        return view
    }

}