package br.digitalhouse.cookbook.ui.dashboard.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.digitalhouse.cookbook.R
import br.digitalhouse.cookbook.data.utils.Base64Custom
import br.digitalhouse.cookbook.data.utils.ConfigFirebase
import br.digitalhouse.cookbook.databinding.FragmentMenuBinding
import br.digitalhouse.cookbook.databinding.LayoutBottomSheetAccBinding
import br.digitalhouse.cookbook.databinding.LayoutBottomSheetBinding
import br.digitalhouse.cookbook.databinding.LayoutBottomSheetPassBinding
import br.digitalhouse.cookbook.ui.signin.model.User
import br.digitalhouse.cookbook.ui.signin.view.SignInActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File

class MenuFragment : Fragment(R.layout.fragment_menu) {
    private val binding: FragmentMenuBinding by lazy { FragmentMenuBinding.inflate(layoutInflater) }
    private val sheetBinding: LayoutBottomSheetBinding by lazy { LayoutBottomSheetBinding.inflate(layoutInflater, null, false) }
    private val sheetBindingAcc: LayoutBottomSheetAccBinding by lazy { LayoutBottomSheetAccBinding.inflate(layoutInflater, null, false) }
    private val sheetBindingPass: LayoutBottomSheetPassBinding by lazy { LayoutBottomSheetPassBinding.inflate(layoutInflater, null, false) }
    private var auth: FirebaseAuth? = null
    private val IMAGE_CAPTURE_CODE = 1
    private val IMAGE_STORAGE_CODE = 2
    private lateinit var firebaseStorage: FirebaseStorage
    private val firebaseRef = ConfigFirebase().getFirebase()
    private lateinit var storageReference: StorageReference
    private var valueEventListener: ValueEventListener? = null
    private lateinit var alertDialog: AlertDialog
    private var resultBitMap: Bitmap? = null
    private var photoUri: Uri? = null
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

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
        firebaseStorage = FirebaseStorage.getInstance()
        FirebaseDatabase.getInstance().getReference("usuarios")
        setData()
        binding.progressBar.visibility = View.GONE
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.visibility = View.GONE
        setImageClient()
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
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 10)
            }else {
                val intentCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intentCapture, IMAGE_CAPTURE_CODE)}
        } else {
            val build = AlertDialog.Builder(requireContext(), R.style.ThemeCustomDialog)
            val view = layoutInflater.inflate(R.layout.alert_dialog_gallery_cam, null)
            build.setView(view)
            val btnCam = view.findViewById<ImageButton>(R.id.camera_ib)
            val btnGal = view.findViewById<ImageButton>(R.id.gallery_ib)
            val btnCancel = view.findViewById<TextView>(R.id.cancel_tv)
            btnCancel.setOnClickListener{ alertDialog.dismiss() }
            btnCam.setOnClickListener{
                binding.progressBar.visibility = View.VISIBLE
                openCam()
                alertDialog.dismiss()}
            btnGal.setOnClickListener{
                binding.progressBar.visibility = View.VISIBLE
                openGal()
                alertDialog.dismiss()}
            alertDialog = build.create()
            alertDialog.show()
        }
    }

    private fun openCam() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), IMAGE_CAPTURE_CODE)
        } else {
            val intentCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intentCapture, IMAGE_CAPTURE_CODE)
        }
    }

    private fun openGal() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), IMAGE_STORAGE_CODE
            )
        } else {
            val intentStorage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentStorage, IMAGE_STORAGE_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == IMAGE_CAPTURE_CODE && data != null) {
                val foto = data.getParcelableExtra<Bitmap>("data")
                // binding.imageViewClient.setImageBitmap(foto)
                val extras = data.extras
                val img = extras!!.get("data") as Bitmap
                //binding.imageViewClient.setImageBitmap(img)
                val tempUri = getImageUri(requireContext(), img)
                val finalFile = File(getRealPathFromURI(tempUri))
                resultBitMap = img
                photoUri = tempUri!!

                //função adicionar foto no firebase
                storageReference = FirebaseStorage.getInstance().getReference("usuarios/"+auth!!.currentUser!!.uid)
                storageReference.putFile(photoUri!!).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Imagem alterada!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(requireContext(), "Não foi possível alterar a imagem.", Toast.LENGTH_SHORT).show()
                }
                binding.progressBar.visibility = View.GONE
            } else if (requestCode == IMAGE_STORAGE_CODE && data != null) {
                val selectedPhotoUri = data.data
                val imgPath = getRealPathFromURI(selectedPhotoUri)
                val finalFileS = File(imgPath.toString())
                try {
                    selectedPhotoUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedPhotoUri)
                            //binding.imageViewClient.setImageBitmap(bitmap)
                            resultBitMap = bitmap
                            photoUri = selectedPhotoUri
                        } else {
                            val source = ImageDecoder.createSource(requireContext().contentResolver, selectedPhotoUri)
                            val bitmapT = ImageDecoder.decodeBitmap(source)
                            //binding.imageViewClient.setImageBitmap(bitmapT)
                            resultBitMap = bitmapT
                            photoUri = selectedPhotoUri
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                //função adicionar foto no firebase
                storageReference = FirebaseStorage.getInstance().getReference("usuarios/"+auth!!.currentUser!!.uid)
                storageReference.putFile(photoUri!!).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Imagem alterada!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(requireContext(), "Não foi possível alterar a imagem.", Toast.LENGTH_SHORT).show()
                }
                binding.progressBar.visibility = View.GONE
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage, "image", null
        )
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(uri: Uri?): String? {
        var path = ""
        if (requireContext().contentResolver != null) {
            val cursor: Cursor? = requireContext().contentResolver.query(
                uri!!,
                null, null, null, null
            )
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == IMAGE_CAPTURE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, IMAGE_CAPTURE_CODE)
            }
        }

        if (requestCode == IMAGE_STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intentG =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentG, IMAGE_STORAGE_CODE)
            }
        }
    }

    private fun setImageClient() {
        try {
            firebaseStorage.reference.child("usuarios/").child(auth!!.currentUser!!.uid)
                .downloadUrl.addOnSuccessListener { uri ->
                    Glide
                        .with(this)
                        .load(uri)
                        .error(R.drawable.user)
                        .into(binding.imageViewClient)
                }
        }catch (e: Exception){
            Log.e("STORAGE", "USUARIO SEM LOGIN")
        }
    }

    private fun setData(){
        val userAuth = auth!!.currentUser
        val idUsuario = Base64Custom.codificarBase64(userAuth!!.email)
        val nameUser = firebaseRef!!.child("usuarios").child(idUsuario)
        valueEventListener = nameUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                binding.textNameUser.text = user!!.email
            }
            override fun onCancelled(error: DatabaseError) {}
        })
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