package br.digitalhouse.cookbook.ui.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding: FragmentSearchBinding by lazy { FragmentSearchBinding.inflate(layoutInflater) }

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
