package br.digitalhouse.cookbook.ui.dashboard.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import br.digitalhouse.alugueapp.mockkdata.BannerDataClass
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.databinding.FragmentHomeBinding
import br.digitalhouse.cookbook.ui.dashboard.adapter.HomeBannerAdapter
import br.digitalhouse.cookbook.ui.dashboard.adapter.HomeRecipeAdapter
import br.digitalhouse.cookbook.ui.dashboard.viewmodel.RecipesViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home){
    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var bannerAdapter: HomeBannerAdapter
    private lateinit var homeRecipeAdapter: HomeRecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomeBannerAdapter()
        initHomeListAdapter()
    }

    private fun initHomeBannerAdapter() {
        bannerAdapter = HomeBannerAdapter(
            listOf(
                BannerDataClass(image = R.drawable.bolocenoura, text = "Bolo de cenoura"),
                BannerDataClass(image = R.drawable.pudim, text = "Pudim de leite"),
                BannerDataClass(image = R.drawable.tortacafe, text = "Torta de café"),
                BannerDataClass(image = R.drawable.mussemaracuja, text = "Mousse de maracujá"),
                BannerDataClass(image = R.drawable.cheesecake, text = "Cheesecake de morango"),
                BannerDataClass(image = R.drawable.tortadefrango, text = "Torta de frango"),
                BannerDataClass(image = R.drawable.salpicao, text = "Salpicão"),
                BannerDataClass(image = R.drawable.strogonoff, text = "Strogonoff"),
                BannerDataClass(image = R.drawable.lasanha, text = "Lasanha de carne"),
                BannerDataClass(image = R.drawable.pizza, text = "Pizza de Calabresa")
            )
        )
        val bannerViewPager = binding.rvItemHomeBanner
        bannerViewPager.adapter = bannerAdapter
        bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        })
        bannerAdapter.setOnClickListener(object : HomeBannerAdapter.ItemClickListener{
            override fun onItemClick(position: Int) {
                //bundle com informações
            }
        })
        (bannerViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        binding.floatButtonNext.setOnClickListener{
            if (bannerViewPager.currentItem +1 <bannerAdapter.itemCount){
                bannerViewPager.currentItem +=1
            }
        }
        binding.floatButtonBack.setOnClickListener{
            if (bannerViewPager.currentItem -1 <bannerAdapter.itemCount){
                bannerViewPager.currentItem -=1
            }
        }
    }

    private fun initHomeListAdapter(){
        homeRecipeAdapter = HomeRecipeAdapter(requireContext(), onItemClicked = { name, num, image, height, weight, type, weaknesses, prevevo, nextevo ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("num", num)
            intent.putExtra("image", image)
            intent.putExtra("height", height)
            intent.putExtra("weight", weight)
            intent.putExtra("type", type)
            intent.putExtra("weaknesses", weaknesses)
            intent.putExtra("prevevo", prevevo)
            intent.putExtra("nextevo", nextevo)
            startActivity(intent)
        })
        requireView().findViewById<RecyclerView>(R.id.rvListRecipes).adapter = homeRecipeAdapter

        lifecycleScope.launch{
            val feed = viewModel.fetchRecipes()
            homeRecipeAdapter.update(feed)
        }
    }
}