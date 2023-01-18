package br.digitalhouse.cookbook.ui.intro.view

import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.data.utils.NetworkReceiver
import br.digitalhouse.cookbook.databinding.ActivitySlideBinding
import br.digitalhouse.cookbook.ui.intro.adapter.SlideAdapter
import br.digitalhouse.cookbook.ui.intro.model.SlideDataClass
import br.digitalhouse.cookbook.ui.signin.view.SignInActivity

class SlideActivity : AppCompatActivity() {
    private val binding: ActivitySlideBinding by lazy { ActivitySlideBinding.inflate(layoutInflater) }
    private lateinit var introSlideAdapter: SlideAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnBoardingItems()
        setUpIndicators()
        setCurrentIndicator(0)
    }

    private fun setOnBoardingItems() {
        introSlideAdapter = SlideAdapter(
            listOf(
                SlideDataClass(image = R.drawable.facebook, description = "Bem vindo ao Gastro Cook's, seu hand book de receitas!"),
                SlideDataClass(image = R.drawable.googlelogo, description = "São mais de 8 mil receitas profissionais na palma da sua mão!"),
            )
        )
        val slideViewPager = findViewById<ViewPager2>(R.id.fragment_onboarding_slide_viewpager)
        slideViewPager.adapter = introSlideAdapter
        slideViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (slideViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        binding.imageNext.setOnClickListener{
            if (slideViewPager.currentItem +1 <introSlideAdapter.itemCount){
                slideViewPager.currentItem +=1
            }else{
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
    }

    private fun setUpIndicators() {
        val indicators = arrayOfNulls<ImageView>(introSlideAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                binding.fragmentOnboardingSlideIndicator.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val indicatorsContainer = binding.fragmentOnboardingSlideIndicator
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_background
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
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

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(NetworkReceiver(), intentFilter)
    }
}