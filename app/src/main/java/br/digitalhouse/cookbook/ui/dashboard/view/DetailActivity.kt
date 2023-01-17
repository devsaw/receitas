package br.digitalhouse.cookbook.ui.dashboard.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.digitalhouse.cookbook.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}