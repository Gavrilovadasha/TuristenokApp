package daa.gv.turistenokapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Инициализация Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Инициализация кнопки аватара
        avatarButton = view.findViewById(R.id.btn_avatar)

        // Получаем кнопку "Выйти" по ID
        val exitButton: TextView = view.findViewById(R.id.exit_profile)

        // Устанавливаем обработчик на кнопку "Выйти"
        exitButton.setOnClickListener {
            // Выход из аккаунта Firebase
            auth.signOut()

            // Перенаправляем на фрагмент авторизации или главную страницу
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(
                R.id.container,
                AvtorizFragment()
            )  // Перенаправляем на экран авторизации
            transaction.addToBackStack(null)  // Добавляем в back stack
            transaction.commit()

            // Выводим уведомление об успешном выходе
            Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
        }

        // Регистрация результата выбора изображения
        imagePickerLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                if (selectedImageUri != null) {
                    avatarButton.setImageURI(selectedImageUri) // Установить изображение в кнопку
                    saveImageToInternalStorage(selectedImageUri!!) // Сохранить на устройстве
                }
            }
        }
        avatarButton.setOnClickListener {
            openGallery()
        }

        // Инициализация TextView
        userNameDisplay = view.findViewById(R.id.user_name_display)

        // Загрузка данных пользователя
        loadUserData()

        return view
    }

    private fun openGallery() {
        // Открытие галереи
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun saveImageToInternalStorage(imageUri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            val currentUser = auth.currentUser
            val userId = currentUser?.uid ?: return // Получаем UID пользователя

            // Создаем папку для каждого пользователя, если её нет
            val userFolder = File(requireContext().filesDir, "user_images")
            if (!userFolder.exists()) {
                userFolder.mkdirs()
            }

            // Создаем файл для изображения с именем, основанным на UID
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

            // Сохраняем путь к изображению в SharedPreferences
            saveImageUriToSharedPreferences(file.toUri())

            Toast.makeText(requireContext(), "Фото сохранено локально: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Ошибка сохранения изображения локально", e)
            Toast.makeText(requireContext(), "Ошибка сохранения фото", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveImageUriToSharedPreferences(imageUri: Uri) {
        val sharedPreferences = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("avatar_uri", imageUri.toString()) // Сохраняем URI изображения
        editor.apply()
    }


    private fun loadImageFromSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString("avatar_uri", null)

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            avatarButton.setImageURI(imageUri) // Устанавливаем изображение
        }
    }

    private fun loadImageFromInternalStorage() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: return // Получаем UID пользователя

        // Путь к папке с изображениями
        val userFolder = File(requireContext().filesDir, "user_images")
        val file = File(userFolder, "$userId.jpg")

        if (file.exists()) {
            // Устанавливаем изображение в кнопку аватара
            avatarButton.setImageURI(Uri.fromFile(file))
        } else {
            // Если изображения нет, можно установить дефолтное
            avatarButton.setImageResource(R.drawable.avatar)
        }
    }


    private fun loadUserData() {
        // Получаем текущего пользователя
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Ошибка: Пользователь не найден", Toast.LENGTH_SHORT).show()
            return
        }

        // Загружаем изображение из SharedPreferences
        loadImageFromSharedPreferences()

        // Получаем данные из Firestore по UID
        val userId = currentUser.uid
        firestore.collection("users")
            .document(userId) // Используем UID для поиска
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userName = document.getString("name") ?: "Имя не найдено"
                    userNameDisplay.text = userName
                } else {
                    Toast.makeText(requireContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show()
                }
                // Загрузка изображения после того, как данные пользователя загружены
                loadImageFromInternalStorage()

            }

            .addOnFailureListener { e ->
                Log.e("ProfileFragment", "Ошибка загрузки данных пользователя", e)
                Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
            }
    }
}


