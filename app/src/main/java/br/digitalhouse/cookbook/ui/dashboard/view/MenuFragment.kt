package br.digitalhouse.cookbook.ui.dashboard.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.databinding.FragmentMenuBinding
import br.digitalhouse.cookbook.databinding.LayoutBottomSheetBinding
import br.digitalhouse.cookbook.ui.signin.view.SignInActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth

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
            functionExit()
        }
    }

    private fun functionExit() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottonSheetDialog).apply {
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        val sheetBinding: LayoutBottomSheetBinding =
            LayoutBottomSheetBinding.inflate(layoutInflater, null, false)

        sheetBinding.btnSair.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Você saiu!", Toast.LENGTH_LONG).show()
            startActivity(Intent(requireContext(), SignInActivity::class.java))
        }

        dialog.setContentView(sheetBinding.root)
        dialog.show()
    }
}