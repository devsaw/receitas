package br.digitalhouse.cookbook.ui.intro.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.digitalhouse.cookbook.databinding.ActivitySlideBinding

class SlideActivity : AppCompatActivity() {
    private val binding: ActivitySlideBinding by lazy { ActivitySlideBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}