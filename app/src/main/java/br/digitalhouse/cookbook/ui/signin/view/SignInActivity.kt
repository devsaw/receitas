package br.digitalhouse.cookbook.ui.signin.view

import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.data.utils.NetworkReceiver
import br.digitalhouse.cookbook.databinding.ActivitySignInBinding
import br.digitalhouse.cookbook.ui.signin.adapter.SignInAdapter
import com.google.android.material.tabs.TabLayoutMediator

class SignInActivity : AppCompatActivity() {
    private val binding: ActivitySignInBinding by lazy { ActivitySignInBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initBottomNav()
    }

    private fun initBottomNav() {
        val tabLayout = binding.myTabLayout
        val viewPager = binding.viewPagerTab
        val adapterTab = SignInAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapterTab

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Logar"
                }
                1 -> {
                    tab.text = "Registrar"
                }
            }
        }.attach()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(NetworkReceiver(), intentFilter)
    }
}