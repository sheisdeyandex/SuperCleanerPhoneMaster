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

class FragmentExtremePowerSaving(ultrapowerText:String,iFragment: IFragment,iBanner: IBanner ) : Fragment() {
    val ultrapowerText:String
    val iFragment:IFragment
    val iBanner = iBanner
init {
    this.iFragment = iFragment
    this.ultrapowerText = ultrapowerText
}

    override fun onHiddenChanged(hidden: Boolean) {
        if(!hidden){

        }
    }

    private var _binding: FragmentExtremePowerSavingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtremePowerSavingBinding.inflate(inflater, container, false)
        val view = binding.root
        var   adRequest = AdRequest.Builder().build()
        if(!MyApplication.premiumUser){
            binding.avBanner.loadAd(adRequest)
        }
        binding.tvUltrapowertext.text = binding.tvUltrapowertext.text.toString() +" "+ ultrapowerText
binding.tvHoursText.text = "(+4 ч. 00 м.)"
        binding.materialButtonDoIt.setOnClickListener {
            MyApplication.worktime =  ultrapowerText
            iFragment.regulate(false, 5)
            binding.animationView.setAnimation(R.raw.blue_ellipse)
            binding.animationView.playAnimation()
            binding.animationView.loop(true)
        }
        return view
    }

}