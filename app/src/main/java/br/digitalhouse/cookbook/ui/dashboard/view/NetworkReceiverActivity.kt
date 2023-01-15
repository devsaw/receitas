package br.digitalhouse.cookbook.ui.dashboard.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.digitalhouse.cookbook.databinding.ActivityNetworkReceiverBinding

class NetworkReceiverActivity : AppCompatActivity() {
    private val binding: ActivityNetworkReceiverBinding by lazy { ActivityNetworkReceiverBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}