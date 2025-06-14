package daa.gv.turistenokapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userNameDisplay: TextView
    private lateinit var avatarButton: ImageButton
    private var selectedImageUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Инициализация Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Элементы интерфейса
        avatarButton = view.findViewById(R.id.btn_avatar)
        val exitButton: Button = view.findViewById(R.id.exit_profile)
        userNameDisplay = view.findViewById(R.id.user_name_display)

        // Настройка кнопки выхода
        exitButton.setOnClickListener {
            showLogoutDialog()
        }

        // Регистрация лаунчера для выбора изображения
        registerImagePickerLauncher()

        // Обработчик нажатия на аватар
        avatarButton.setOnClickListener {
            openGallery()
        }

        // Загрузка данных пользователя
        loadUserData()

        // Переход к маршрутам
        view.findViewById<Button>(R.id.btn_my_routes).setOnClickListener {
            val myRoutesFragment = MyRoutesFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, myRoutesFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun registerImagePickerLauncher() {
        imagePickerLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                selectedImageUri?.let {
                    avatarButton.setImageURI(it)
                    saveImageToInternalStorage(it)
                    uploadImageToFirestore(it)
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        builder.setTitle("Выход")
        builder.setMessage("Вы действительно хотите выйти из аккаунта?")

        builder.setPositiveButton("Да") { dialog, which ->
            clearAvatarFromSharedPreferences()
            clearUserImageStorage()
            auth.signOut()

            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AvtorizFragment())
                .addToBackStack(null)
                .commit()

            Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Нет") { dialog, which -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
    }

    private fun saveImageToInternalStorage(imageUri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            val currentUser = auth.currentUser ?: return
            val userId = currentUser.uid

            val userFolder = File(requireContext().filesDir, "user_images")
            if (!userFolder.exists()) {
                userFolder.mkdirs()
            }

            val file = File(userFolder, "$userId.jpg")
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream!!.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()

            saveImageUriToSharedPreferences(file.toUri())
            Toast.makeText(requireContext(), "Фото сохранено локально", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Ошибка сохранения изображения", e)
            Toast.makeText(requireContext(), "Ошибка сохранения фото", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageUriToSharedPreferences(imageUri: Uri) {
        val sharedPreferences = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("avatar_uri", imageUri.toString()).apply()
    }

    private fun clearAvatarFromSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("avatar_uri").apply()
    }

    private fun clearUserImageStorage() {
        val currentUser = auth.currentUser ?: return
        val userFolder = File(requireContext().filesDir, "user_images")
        val file = File(userFolder, "${currentUser.uid}.jpg")
        if (file.exists()) {
            file.delete()
        }
    }

    private fun loadImageFromSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString("avatar_uri", null)
        if (imageUriString != null) {
            avatarButton.setImageURI(Uri.parse(imageUriString))
        } else {
            avatarButton.setImageResource(R.drawable.man_avatar)
        }
    }

    private fun loadImageFromInternalStorage() {
        val currentUser = auth.currentUser ?: return
        val userId = currentUser.uid
        val userFolder = File(requireContext().filesDir, "user_images")
        val file = File(userFolder, "$userId.jpg")
        if (file.exists()) {
            avatarButton.setImageURI(Uri.fromFile(file))
        } else {
            avatarButton.setImageResource(R.drawable.man_avatar)
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
          //  Toast.makeText(requireContext(), "Ошибка: Пользователь не найден", Toast.LENGTH_SHORT).show()
            return
        }

        // Сначала пробуем загрузить из SharedPreferences или файла
        loadImageFromSharedPreferences()
        loadImageFromInternalStorage()

        val userId = currentUser.uid
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name") ?: "Имя не задано"
                    userNameDisplay.text = name

                    // Получаем Base64 из Firestore
                    val base64Image = document.getString("avatar_base64")
                    if (!base64Image.isNullOrEmpty()) {
                        // Декодируем и устанавливаем аватар
                        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
                        val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        avatarButton.setImageBitmap(decodedBitmap)

                        // Сохраняем изображение локально для следующих запусков
                        saveBitmapToInternalStorage(decodedBitmap, userId)
                        saveImageUriToSharedPreferences(
                            Uri.fromFile(File(requireContext().filesDir, "user_images/$userId.jpg"))
                        )
                    }
                } else {
                    Toast.makeText(requireContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("ProfileFragment", "Ошибка загрузки данных", e)
                Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveBitmapToInternalStorage(bitmap: Bitmap, userId: String) {
        try {
            val userFolder = File(requireContext().filesDir, "user_images")
            if (!userFolder.exists()) {
                userFolder.mkdirs()
            }

            val file = File(userFolder, "$userId.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Ошибка сохранения Bitmap", e)
        }
    }
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun uploadImageToFirestore(imageUri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            val base64Image = bitmapToBase64(bitmap)

            auth.currentUser?.uid?.let { userId ->
                firestore.collection("users").document(userId)
                    .update("avatar_base64", base64Image)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Фото обновлено", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e("ProfileFragment", "Ошибка отправки фото в Firestore", e)
                        Toast.makeText(requireContext(), "Ошибка сохранения фото", Toast.LENGTH_SHORT).show()
                    }
            }
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Ошибка преобразования изображения", e)
            Toast.makeText(requireContext(), "Ошибка обработки фото", Toast.LENGTH_SHORT).show()
        }
    }
}