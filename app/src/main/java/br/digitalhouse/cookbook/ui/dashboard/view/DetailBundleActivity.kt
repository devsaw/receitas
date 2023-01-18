package br.digitalhouse.cookbook.ui.dashboard.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.digitalhouse.cookbook.databinding.ActivityDetailBundleBinding

class DetailBundleActivity : AppCompatActivity() {
    private val binding: ActivityDetailBundleBinding by lazy { ActivityDetailBundleBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}