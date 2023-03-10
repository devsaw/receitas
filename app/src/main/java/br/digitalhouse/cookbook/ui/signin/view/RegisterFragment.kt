package br.digitalhouse.cookbook.ui.signin.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.data.utils.Base64Custom
import br.digitalhouse.cookbook.data.utils.ConfigFirebase
import br.digitalhouse.cookbook.data.utils.Preferences
import br.digitalhouse.cookbook.databinding.FragmentRegisterBinding
import br.digitalhouse.cookbook.ui.signin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.lang.Exception

class RegisterFragment : Fragment(R.layout.fragment_register){
    private val binding: FragmentRegisterBinding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }
    private var auth: FirebaseAuth? = null
    lateinit var preferences: Preferences
    private var usuario: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = Preferences(requireContext())
        binding.progressBar.visibility = View.GONE
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btRegister.setOnClickListener{
            val name = binding.editTextNameRegister.text.toString()
            val email = binding.editTextUserRegister.text.toString()
            val password = binding.editTextPassRegister.text.toString()
            val coPassword = binding.editTwoTextPassRegister.text.toString()
            val checkBox = binding.checkBoxTermoDeUso
            if (name.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || coPassword.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != coPassword){
                Toast.makeText(requireContext(), "As senhas devem ser iguais!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!checkBox.isChecked){
                Toast.makeText(requireContext(), "Aceite os termos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            usuario = User()
            usuario!!.nome = name
            preferences.setInforUserName(usuario!!.nome!!)
            usuario!!.email = email
            usuario!!.senha = password
            createUser()
        }

        binding.textTerms.setOnClickListener{
            startActivity(Intent(requireContext(), PdfViewerActivity::class.java))
        }
    }

    private fun createUser() {
        binding.progressBar.visibility = View.VISIBLE
        auth = ConfigFirebase().getAuth()
        auth!!.createUserWithEmailAndPassword(
            usuario!!.email.toString(), usuario!!.senha.toString()
        ).addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                auth!!.currentUser!!.sendEmailVerification()
                    .addOnSuccessListener {
                        val idUsuario = Base64Custom.codificarBase64(usuario!!.email)
                        usuario!!.idUsuario = idUsuario
                        usuario!!.saveUser()
                        Toast.makeText(requireContext(), "Cadastro efetuado, verifique seu e-mail!", Toast.LENGTH_LONG).show()
                        startActivity(Intent(requireContext(), SignInActivity::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Erro ao se cadastrar!", Toast.LENGTH_LONG).show()
                        Log.e("ERRO", "Erro ao se cadastrar, ${it.toString()}")
                    }
            } else {
                binding.progressBar.visibility = View.GONE
                var excecao = ""
                try {
                    throw it.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    excecao = "Digite uma senha mais forte!"
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    println("${e.message}")
                    excecao = "Digite um e-mail v??lido!"
                } catch (e: FirebaseAuthUserCollisionException) {
                    excecao = "Esse email j?? est?? em uso!"
                } catch (e: Exception) {
                    excecao = "Erro ao cadastrar usu??rio: " + e.message
                    e.printStackTrace()
                }
                Toast.makeText(requireContext(), excecao, Toast.LENGTH_LONG).show()
            }
        }
        hideKeyboard(requireView())
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}