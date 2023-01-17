package br.digitalhouse.cookbook.ui.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.databinding.FragmentHomeBinding
import br.digitalhouse.cookbook.ui.dashboard.viewmodel.RecipesViewModel

class HomeFragment : Fragment(R.layout.fragment_home){
    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}