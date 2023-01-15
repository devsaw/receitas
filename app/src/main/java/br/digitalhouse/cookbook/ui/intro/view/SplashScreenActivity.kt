package br.digitalhouse.cookbook.ui.intro.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.data.utils.ConfigFirebase
import br.digitalhouse.cookbook.databinding.ActivitySplashscreenBinding
import br.digitalhouse.cookbook.ui.dashboard.view.DashBoardActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val binding: ActivitySplashscreenBinding by lazy { ActivitySplashscreenBinding.inflate(layoutInflater) }
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        splashFun()
    }

    private fun splashFun() {
        val logo: ImageView = findViewById(R.id.logoSplash)
        logo.alpha=0f
        logo.animate().setDuration(3000).alpha(1f).withEndAction{
            auth = ConfigFirebase().getAuth()

            if (auth!!.currentUser != null) {
                startActivity(Intent(this, DashBoardActivity::class.java))
            } else {
                startActivity(Intent(this, SlideActivity::class.java))
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}