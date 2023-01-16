package br.digitalhouse.cookbook.ui.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.databinding.FragmentMenuBinding

class MenuFragment : Fragment(R.layout.fragment_menu){
    private val binding: FragmentMenuBinding by lazy { FragmentMenuBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnOpenCam.setOnClickListener{

        }

        binding.alterarSenha.setOnClickListener{

        }

        binding.contribuaPix.setOnClickListener{

        }

        binding.sair.setOnClickListener{

        }
    }
}