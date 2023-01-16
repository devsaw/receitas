package br.digitalhouse.cookbook.ui.intro.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.digitalhouse.cookbook.databinding.ActivitySlideBinding
import br.digitalhouse.cookbook.ui.dashboard.view.DashBoardActivity
import br.digitalhouse.cookbook.ui.signin.view.SignInActivity

class SlideActivity : AppCompatActivity() {
    private val binding: ActivitySlideBinding by lazy { ActivitySlideBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClickOnListener()
    }

    private fun setOnClickOnListener(){
        binding.go.setOnClickListener{
            Intent(Intent(this, SignInActivity::class.java))
        }
    }
}