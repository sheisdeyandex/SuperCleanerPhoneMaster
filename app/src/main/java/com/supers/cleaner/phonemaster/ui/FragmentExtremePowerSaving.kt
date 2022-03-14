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
import com.supers.cleaner.phonemaster.databinding.FragmentExtremePowerSavingBinding
import com.supers.cleaner.phonemaster.interfaces.IBanner
import com.supers.cleaner.phonemaster.interfaces.IFragment

class FragmentExtremePowerSaving : Fragment() {
    private var _binding: FragmentExtremePowerSavingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtremePowerSavingBinding.inflate(inflater, container, false)
        val view = binding.root
        (requireActivity() as MainActivity).binding.bnvNav.visibility = View.GONE
         binding.tvUltrapowertext.text = binding.tvUltrapowertext.text.toString() +" "+ MyApplication.finalExtremeModeUsageTime
binding.tvHoursText.text = "(+4 ч. 00 м.)"
        binding.materialButtonDoIt.setOnClickListener {
            MyApplication.worktime =  MyApplication.finalExtremeModeUsageTime
            (requireActivity() as MainActivity).selectTab("extrememodeanim")
            binding.animationView.setAnimation(R.raw.blue_ellipse)
            binding.animationView.playAnimation()
            binding.animationView.loop(true)
        }

        return view
    }

}