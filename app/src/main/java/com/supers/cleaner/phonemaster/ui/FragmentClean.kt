package com.supers.cleaner.phonemaster.ui
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.databinding.FragmentCleanBinding
import com.supers.cleaner.phonemaster.interfaces.IBanner
import com.supers.cleaner.phonemaster.interfaces.IFragment

class FragmentClean : Fragment() {

    private var _binding: FragmentCleanBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentCleanBinding.inflate(inflater, container, false)
        val view = binding.root
if(MyApplication.clean){
changeAnim()
}
        binding.materialButtonDoIt.setOnClickListener {
            (requireActivity() as MainActivity).selectTab("cleananim")
            (requireActivity() as MainActivity).binding.bnvNav.visibility= View.GONE
changeAnim()
        }
        return view
    }
    fun changeAnim(){
        object :CountDownTimer(2000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                binding.animationView.setAnimation(R.raw.blue_ellipse)
                binding.materialButtonDoIt.isClickable = false
                binding.tvAlot1.text = getString(R.string.cleaned)
                binding.tvAlot2.text = getString(R.string.cleaned)
                binding.tvAlot3.text = getString(R.string.cleaned)
                binding.tvAlot4.text = getString(R.string.cleaned)
                binding.tvOverheat.text = getString(R.string.crystal_clear)
                binding.tvOverheat.setTextColor(Color.parseColor("#73EE48"))
                binding.ivComponent1.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_component_1_1))
                binding.ivComponent2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_component_2_1))
                binding.ivComponent3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_component_4_1))
                binding.ivComponent4.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_component_3_1))
                binding.tvAlot1.setTextColor(Color.parseColor("#73EE48"))
                binding.tvAlot2.setTextColor(Color.parseColor("#73EE48"))
                binding.tvAlot3.setTextColor(Color.parseColor("#73EE48"))
                binding.tvAlot4.setTextColor(Color.parseColor("#73EE48"))
                binding.materialButtonDoIt.background = ContextCompat.getDrawable(requireContext(), R.drawable.optimize_button_optimized)
                binding.materialButtonDoIt.text = getText(R.string.cleaned_button)
                binding.ivClean.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_clean_cleaned))
            }

        }.start()
    }
}