package com.supers.cleaner.phonemaster.ui
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.supers.cleaner.phonemaster.MyApplication
import com.supers.cleaner.phonemaster.R
import com.supers.cleaner.phonemaster.Utils.AlarmUtils
import com.supers.cleaner.phonemaster.databinding.ActivityMainBinding
import com.supers.cleaner.phonemaster.interfaces.AskPermissions
import com.supers.cleaner.phonemaster.interfaces.IFragment
import com.supers.cleaner.phonemaster.services.BatterySaverReceiver
import com.supers.cleaner.phonemaster.services.CleanBroadcastReceiver
import com.supers.cleaner.phonemaster.services.WidgetBroadCastReceiver
import java.util.*


class MainActivity : AppCompatActivity(), IFragment, AskPermissions{
    lateinit var binding:ActivityMainBinding
    val fm = supportFragmentManager
    var mInterstitialAd: InterstitialAd? = null
    lateinit var adRequest: AdRequest
    lateinit var finalExtremeModeUsageTime:String
    private lateinit var navController: NavController
    private val navigator =object: AppNavigator(this, R.id.cl_main){
        override fun setupFragmentTransaction(
            screen: FragmentScreen,
            fragmentTransaction: FragmentTransaction,
            currentFragment: Fragment?,
            nextFragment: Fragment)
        {}
    }
    override fun requestBlueTooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            askForPermission(android.Manifest.permission.BLUETOOTH_CONNECT, 1)
        }
        else{
            MyApplication.bluetoothPermissionGranted = true
        }
    }

    private fun askForPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(applicationContext, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    permission
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        else{
            MyApplication.bluetoothPermissionGranted = true
        }
    }
//    private fun setCustomNotification(){
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notificationLayout = RemoteViews("com.supers.cleaner.phonemaster", R.layout.custom_notification_layout)
////    val notificationIntent = Intent()
////  //  intent.putExtra("whattodo","boost")
////    val stackBuilder = TaskStackBuilder.create(applicationContext)
////stackBuilder.addNextIntent(notificationIntent)
//////
////    val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
////////
////    notificationLayout.setOnClickPendingIntent(R.id.ll_boost, pendingIntent)
//        val customNotification = NotificationCompat.Builder(applicationContext, AlarmReceiver.CHANNEL_ID)
//            .setSmallIcon(android.R.color.transparent)
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//            .setCustomContentView(notificationLayout)
//            .setOngoing(true)
//            .build()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                AlarmReceiver.CHANNEL_ID,
//                resources.getString(R.string.app_name),
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//        notificationManager.notify(0, customNotification)
//    }
    private fun setNotification() {

        val calendar = Calendar.getInstance()
        val now = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 5)
        calendar.set(Calendar.MINUTE,12)
        calendar.set(Calendar.SECOND, 0)

        //if user sets the alarm after their preferred time has already passed that day
        if (now.after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        val intent = Intent(this, CleanBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission with request code 1 granted
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
            } else {
                //permission with request code 1 was not granted
                Toast.makeText(this, "Permission was not Granted", Toast.LENGTH_LONG).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    object Screens {
        fun fragmentOptimize() = FragmentScreen { FragmentOptimize() }
        fun fragmentEnergySaving() = FragmentScreen { FragmentEnergySaving() }
        fun fragmentCoolCPU() = FragmentScreen { FragmentCoolCpu() }
        fun fragmentCoolCPUAnim() = FragmentScreen { FragmentCoolCpuAnim() }
        fun fragmentClean() = FragmentScreen { FragmentClean() }
        fun fragmentCleanAnim() = FragmentScreen { FragmentCleanAnim() }
        fun fragmentNoAds() = FragmentScreen { FraFragmentNoAds() }
        fun fragmentSplash() = FragmentScreen { SplashScreen() }
        fun fragmentNormalMode() = FragmentScreen { FragmentNormalMode() }
        fun fragmentExtremePowerSaving() = FragmentScreen { FragmentExtremePowerSaving() }
        fun fragmentExtremePowerSavingAnim() = FragmentScreen { FragmentExtremePowerSavingAnim() }
        fun fragmentUltraPowerSaving() = FragmentScreen { FragmentUltraPowerSaving() }
        fun fragmentUltraPowerSavingAnim() = FragmentScreen { FragmentUltraPowerSavingAnim() }
        fun fragmentAppsManager() = FragmentScreen { FragmentApps() }
    }
    override fun onResume() {
        super.onResume()
        MyApplication.sInstance.navigatorHolder.setNavigator(navigator)
    }

fun selectTab(tab: String) {
    val fm = supportFragmentManager
    var currentFragment: Fragment? = null
    val fragments = fm.fragments
    for (f in fragments) {
        if (f.isVisible) {
            currentFragment = f
            break
        }
    }
    val newFragment = fm.findFragmentByTag(tab)
    if (currentFragment != null && newFragment != null && currentFragment === newFragment) return
    val transaction = fm.beginTransaction()
    if (newFragment == null) {
        if(tab=="optimize"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentOptimize().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="energysaving"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentEnergySaving().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="coolcpu"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentCoolCPU().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="coolcpuanim"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentCoolCPUAnim().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="energysaving"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentEnergySaving().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="clean"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentClean().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="cleananim"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentCleanAnim().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="noads"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentNoAds().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="splash"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentSplash().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="ultramode"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentUltraPowerSaving().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="ultramodeanim"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentUltraPowerSavingAnim().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="extrememode"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentExtremePowerSaving().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="extrememodeanim"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentExtremePowerSavingAnim().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="normalmode"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentNormalMode().createFragment(fm.fragmentFactory)
                , tab
            )
        }
        else if(tab=="appsmanager"){
            transaction.add(
                R.id.cl_main,
                Screens.fragmentAppsManager().createFragment(fm.fragmentFactory)
                , tab
            )
        }
    }
    if (currentFragment != null) {
        transaction.hide(currentFragment)
    }
    if (newFragment != null) {
        transaction.show(newFragment)
    }
    transaction.commitNow()
}
    lateinit var  mBatteryStatusReceiver:BatterySaverReceiver
    val BROADCAST = "com.supers.cleaner.phonemaster.android.action.broadcast"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
manager.cancel(2)
manager.cancel(1)
        AlarmUtils.setAlarm(this,
            AlarmUtils.ACTION_CHECK_DEVICE_STATUS,
            AlarmUtils.TIME_CHECK_DEVICE_STATUS)
        val adRequest = AdRequest.Builder().build()
        if(!MyApplication.premiumUser){
            binding.avBanner.loadAd(adRequest)
        }
        if (intent.hasExtra("whattodo")) {
            MyApplication.notificationClicked = true
            binding.clSplash.visibility= View.GONE

            binding.clOptimize.visibility = View.VISIBLE

            if(intent.extras?.get("whattodo").toString() == "optimize"){
                selectTab("optimize")
                MyApplication.optimize = true
            }
            if(intent.extras?.get("whattodo").toString() == "energysaving"){
            //    selectTab("energysaving")
                binding.bnvNav.selectedItemId = R.id.nav_energy_saving
                binding.clSplash.visibility = View.VISIBLE
binding.clOptimize.visibility = View.GONE
                MyApplication.energysaving = true
            }
            if(intent.extras?.get("whattodo").toString() == "coolcpu"){
                selectTab("coolcpuanim")
                binding.bnvNav.selectedItemId = R.id.fragmentCoolCPU
                MyApplication.coolcpu = true
            }
            if(intent.extras?.get("whattodo").toString() == "clean"){
                binding.bnvNav.selectedItemId = R.id.fragmentClean
                selectTab("cleananim")
                MyApplication.clean = true
            }
            if(intent.extras?.get("whattodo").toString() == "others"){
                binding.bnvNav.selectedItemId = R.id.no_ads
                binding.clSplash.visibility = View.VISIBLE
                binding.clOptimize.visibility = View.GONE

                MyApplication.others = true
            }
        } else {
            // Do something else
        }


        initEnergySavingAnims()
        binding.bnvNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_graph_home -> {
                    selectTab("optimize")
                    true
                }
                R.id.nav_energy_saving -> {
                    selectTab("energysaving")
                    true
                }
                R.id.fragmentCoolCPU ->{
                    selectTab("coolcpu")
                    true
                }
                R.id.fragmentClean ->{
                    selectTab("clean")
                    true
                }
                R.id.no_ads ->{
                    selectTab("noads")
                    true
                }
                else -> false
            }}

        initInter()
        requestBlueTooth()
        val intent = Intent(BROADCAST)
        val intentFilter = IntentFilter(BROADCAST)
        registerReceiver(WidgetBroadCastReceiver(), intentFilter)
sendBroadcast(intent)
        setNotification()
    }
fun initEnergySavingAnims(){
    val infielder = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus = registerReceiver(null, infielder)
    val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    MyApplication.finalUltraModeUsageTime = String.format("%.02f", level*10/100F-0.5F+2).replace(".", " ч. ").replace(",", " ч. ")+" м."
    MyApplication.finalExtremeModeUsageTime = String.format("%.02f", level*10/100F-0.5F+4).replace(".", " ч. ").replace(",", " ч. ")+" м."
}
fun initInter() {
    adRequest = AdRequest.Builder().build()
    MobileAds.initialize(this) {}
    InterstitialAd.load(
        this,
        getString(R.string.inter_id),
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                if (!MyApplication.showuserpolicy) {
                    mInterstitialAd = null
                    if(!MyApplication.notificationClicked){

                        selectTab("optimize")
                        binding.clSplash.visibility= View.GONE
                        binding.clOptimize.visibility = View.VISIBLE
                    }
                    else{
                        if(intent.extras?.get("whattodo").toString() == "energysaving"){
                            selectTab("energysaving")
                            binding.clSplash.visibility= View.VISIBLE
                            binding.clOptimize.visibility = View.GONE
                        }
                        else if(intent.extras?.get("whattodo").toString() == "others"){
                            selectTab("noads")
                            binding.clSplash.visibility= View.VISIBLE
                            binding.clOptimize.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                if(intent.extras?.get("whattodo").toString() == "energysaving"){
                    interstitialAd.show(this@MainActivity)
                      selectTab("energysaving")

                    object :CountDownTimer(1000,1000){
                        override fun onTick(p0: Long) {

                        }

                        override fun onFinish() {
                            binding.clSplash.visibility= View.GONE

                            binding.clOptimize.visibility = View.VISIBLE
                        }
                    }.start()
                }
                else   if(intent.extras?.get("whattodo").toString() == "others"){
                    interstitialAd.show(this@MainActivity)
                    selectTab("noads")

                    object :CountDownTimer(1000,1000){
                        override fun onTick(p0: Long) {

                        }

                        override fun onFinish() {
                            binding.clSplash.visibility= View.GONE

                            binding.clOptimize.visibility = View.VISIBLE
                        }
                    }.start()
                }
                else{
                    if(!MyApplication.showuserpolicy) {
                        if(!MyApplication.notificationClicked){
                            selectTab("optimize")
                            interstitialAd.show(this@MainActivity)

                            object :CountDownTimer(1000,1000){
                                override fun onTick(p0: Long) {

                                }

                                override fun onFinish() {
                                    binding.clSplash.visibility= View.GONE

                                    binding.clOptimize.visibility = View.VISIBLE
                                }
                            }.start()
                        }

                    }
                }
                mInterstitialAd = interstitialAd
            }
        })


}
}