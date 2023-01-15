package br.digitalhouse.cookbook.ui.dashboard.view

import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.data.utils.NetworkReceiver
import br.digitalhouse.cookbook.databinding.ActivityDashboardBinding

class DashBoardActivity : AppCompatActivity() {
    private val binding: ActivityDashboardBinding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initBottomNavDashBoard()
    }

    private fun initBottomNavDashBoard(){
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_container)
        setupWithNavController(binding.bottomNavigation, navController)
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