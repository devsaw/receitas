package br.digitalhouse.cookbook.ui.dashboard.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.databinding.FragmentMenuBinding
import br.digitalhouse.cookbook.databinding.LayoutBottomSheetAccBinding
import br.digitalhouse.cookbook.databinding.LayoutBottomSheetBinding
import br.digitalhouse.cookbook.databinding.LayoutBottomSheetPassBinding
import br.digitalhouse.cookbook.ui.signin.view.SignInActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class MenuFragment : Fragment(R.layout.fragment_menu) {
    private val binding: FragmentMenuBinding by lazy { FragmentMenuBinding.inflate(layoutInflater) }
    private val sheetBinding: LayoutBottomSheetBinding by lazy { LayoutBottomSheetBinding.inflate(layoutInflater, null, false) }
    private val sheetBindingAcc: LayoutBottomSheetAccBinding by lazy { LayoutBottomSheetAccBinding.inflate(layoutInflater, null, false) }
    private val sheetBindingPass: LayoutBottomSheetPassBinding by lazy { LayoutBottomSheetPassBinding.inflate(layoutInflater, null, false) }
    private var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding.progressBar.visibility = View.GONE
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnOpenCam.setOnClickListener {
            funOpenOptions()
        }

        binding.alterarSenha.setOnClickListener {
           funChangePass()
        }

        binding.contribuaPix.setOnClickListener {

        }

        binding.desativarConta.setOnClickListener {
            funRemoveAcc()
        }

        binding.sair.setOnClickListener {
            funExit()
        }
    }

    private fun funOpenOptions() {

    }

    private fun funChangePass(){
        val dialog = BottomSheetDialog(requireContext(), R.style.BottonSheetDialog).apply {
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE) }
        dialog.setContentView(R.layout.layout_bottom_sheet_pass)

        val btnChangePass = dialog.findViewById<Button>(R.id.btnChangePass)
        val editTextPassChange = dialog.findViewById<EditText>(R.id.editTextPassChange)
        val editTwoTextPassChange = dialog.findViewById<EditText>(R.id.editTwoTextPassChange)
        val editTextConfirmChange = dialog.findViewById<EditText>(R.id.editTextConfirmChange)

        btnChangePass!!.setOnClickListener {
            if (editTextPassChange!!.text.toString()
                    .isNullOrEmpty() || editTwoTextPassChange!!.text.toString()
                    .isNullOrEmpty() || editTextConfirmChange!!.text.toString()
                    .isNullOrEmpty()
            ) {
                Toast.makeText(requireContext(), "Preencha os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (editTwoTextPassChange.text.toString() != editTextConfirmChange.text.toString()) {
                Toast.makeText(requireContext(), "Senhas diferentes!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (editTwoTextPassChange.length() <= 5) {
                Toast.makeText(requireContext(), "Senha fraca!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = auth!!.currentUser
            val credential = EmailAuthProvider.getCredential(user!!.email!!, editTextPassChange.text.toString())
            if (user != null && user.email != null){
                user.reauthenticate(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        user.updatePassword(editTwoTextPassChange.text.toString()).addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), "Senha alterada!", Toast.LENGTH_LONG).show()
                                auth!!.signOut()
                                dialog.dismiss()
                                startActivity(Intent(requireContext(), SignInActivity::class.java))
                            }else{
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), "Não é possível alterar no momento!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Senha atual incorreta!", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Erro!", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun funExit() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottonSheetDialog).apply {
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE) }
        dialog.setContentView(R.layout.layout_bottom_sheet)
        val btnSair = dialog.findViewById<Button>(R.id.btnSair)

        btnSair!!.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            dialog.dismiss()
            Toast.makeText(requireContext(), "Você saiu!", Toast.LENGTH_LONG).show()
            startActivity(Intent(requireContext(), SignInActivity::class.java))
        }
        dialog.show()
    }

    private fun funRemoveAcc() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottonSheetDialog).apply {
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE) }
        dialog.setContentView(R.layout.layout_bottom_sheet_acc)

        val btnRemoveaAcc = dialog.findViewById<Button>(R.id.btnRemoveaAcc)
        val passEditText = dialog.findViewById<EditText>(R.id.passEditText)

       btnRemoveaAcc!!.setOnClickListener {
            if (passEditText!!.text.toString().isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Digite a senha!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user = auth!!.currentUser
            val credential = EmailAuthProvider.getCredential(user!!.email!!, passEditText.text.toString())
            if (user != null && user.email != null) {
                user.reauthenticate(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.delete()
                        Toast.makeText(
                            requireContext(),
                            "Sua conta foi excluída! Esperamos te ver novamente em breve!",
                            Toast.LENGTH_LONG
                        ).show()
                        dialog.dismiss()
                        ActivityCompat.finishAffinity(requireActivity())
                    } else {
                        Toast.makeText(requireContext(), "Senha incorreta!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Não é possível no momento!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dialog.show()
    }
}