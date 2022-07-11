package com.supers.cleaner.phonemaster.ui.premium

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentFragmentPremiumBinding
import com.supers.cleaner.phonemaster.ui.MainActivity

class FragmentPremium : Fragment() {
    private var _binding: FragmentFragmentPremiumBinding? = null
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = FragmentPremium()
    }

    private lateinit var viewModel: FragmentPremiumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragmentPremiumBinding.inflate(inflater, container, false)
        return binding.root
    }
    var month = true
    var year = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[FragmentPremiumViewModel::class.java]
        if ((requireContext() as MainActivity).bp.isConnected){
            binding.tvMonth.text = getString(R.string.monthText) + " " +(requireActivity() as MainActivity).price+"/"+getString(R.string.justMonth)+" "+getString(R.string.monthTextSecond)
            binding.tvYear.text = getString(R.string.monthText) + " " +(requireActivity() as MainActivity).yearPrice+"/"+getString(R.string.justYear)+" "+getString(R.string.monthTextSecond)
            binding.tvInsteadOf.text = ((requireActivity()as MainActivity).priceFloat*12).toString() +(requireActivity()as MainActivity).currency
            binding.tvInsteadOf.paintFlags = binding.tvInsteadOf.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvMonthTop.text = (requireActivity() as MainActivity).price
            binding.tvYearTop.text = (requireActivity() as MainActivity).yearPrice
        }
        binding.ivClose.setOnClickListener {
            (requireActivity() as MainActivity).selectTab("noads")
        }
        binding.mcvMonth.setOnClickListener {
            month = true
            year = false
            binding.mcvMonth.strokeColor = ContextCompat.getColor(requireContext(), R.color.cardSelectedStrokeColor)
            binding.mcvYear.strokeColor = ContextCompat.getColor(requireContext(), R.color.cardStrokeColor)
        }
        binding.mcvYear.setOnClickListener {
            year = true
            month = false
            binding.mcvYear.strokeColor = ContextCompat.getColor(requireContext(), R.color.cardSelectedStrokeColor)
            binding.mcvMonth.strokeColor = ContextCompat.getColor(requireContext(), R.color.cardStrokeColor)
        }
        binding.mbSubscribe.setOnClickListener {
            if (year){
                (requireActivity()as MainActivity).subscribeYearly()
            }
            if (month){
                (requireActivity() as MainActivity).subscribeMonthly()
            }
        }
    }
}