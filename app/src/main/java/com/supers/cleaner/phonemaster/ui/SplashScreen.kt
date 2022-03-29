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
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.interfaces.IFragment


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
            MyApplication.showuserpolicy = true
        }
        trackUser()
        return view
    }
    private fun trackUser() {
        var client = InstallReferrerClient.newBuilder(requireContext()).build()
        client.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> sendAnal(client.installReferrer.installReferrer)
                    InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> sendAnal("DEVELOPER_ERROR")
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> sendAnal(
                        "FEATURE_NOT_SUPPORTED"
                    )
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> sendAnal("SERVICE_DISCONNECTED")
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> sendAnal("SERVICE_UNAVAILABLE")
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                sendAnal("onInstallReferrerServiceDisconnected")
            }
        })
    }

    private fun sendAnal(s: String) {
        val clickId = getClickId(s)
        var mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        var bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CAMPAIGN, clickId)
        bundle.putString(FirebaseAnalytics.Param.MEDIUM, clickId)
        bundle.putString(FirebaseAnalytics.Param.SOURCE, clickId)
        bundle.putString(FirebaseAnalytics.Param.ACLID, clickId)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, clickId)
        bundle.putString(FirebaseAnalytics.Param.CP1, clickId)
        bundle.putString(FirebaseAnalytics.Param.VALUE, clickId)
        mFirebaseAnalytics.logEvent("traffic_id", bundle)
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.CAMPAIGN_DETAILS, bundle)
    }
    private fun getClickId(s: String): String {
        var id = s
        if (s.contains("&")) {
            id = s.split("&")[0]
        }
        return id
    }
}