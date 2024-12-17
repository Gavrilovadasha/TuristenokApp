package daa.gv.turistenokapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegFragment : Fragment() {

    private lateinit var nameUser: EditText
    private lateinit var email: EditText
    private lateinit var pass: EditText
    private lateinit var repeatPass: EditText
    private lateinit var btn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инфлейтим layout
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        // Инициализация FirebaseAuth
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Инициализация view-элементов
        nameUser = view.findViewById(R.id.edit_user_name)
        email = view.findViewById(R.id.edit_email)
        pass = view.findViewById(R.id.edit_pass)
        repeatPass = view.findViewById(R.id.edit_pass_repeat)
        btn = view.findViewById(R.id.btn_reg)

        btn.setOnClickListener {
            handleRegistration()
        }

        val avtTxt : TextView = view.findViewById(R.id.txt_sign_in)
        avtTxt.setOnClickListener {
            val fragment = AvtorizFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container,fragment)?.commit()
        }

        return view
    }

    private fun handleRegistration() {
        val userName = nameUser.text.toString().trim()
        val userEmail = email.text.toString().trim()
        val userPassword = pass.text.toString().trim()
        val repeatPassword = repeatPass.text.toString().trim()

        // Проверка полей
        if (userEmail.isEmpty() || userName.isEmpty() || userPassword.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        if (!userEmail.contains("@")) {
            Toast.makeText(requireContext(), "Некорректный адрес электронной почты", Toast.LENGTH_SHORT).show()
            return
        }

        if (userPassword.length < 6) {
            Toast.makeText(requireContext(), "Пароль должен быть не менее 6 символов", Toast.LENGTH_SHORT).show()
            return
        }

        if (userPassword != repeatPassword) {
            Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            return
        }

        // Регистрация пользователя в Firebase Authentication
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Регистрация успешна! Добро пожаловать, $userName", Toast.LENGTH_LONG).show()
                    saveUserDataToFirestore(userName, userEmail)
                } else {
                    Toast.makeText(requireContext(), "Ошибка регистрации: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveUserDataToFirestore(userName: String, userEmail: String) {
        val userData = hashMapOf(
            "name" to userName,
            "email" to userEmail
        )
        val userId = auth.currentUser?.uid ?: return // Получаем UID текущего пользователя

        // Сохраняем данные пользователя с его UID как ID документа
        db.collection("users")
            .document(userId)  // Используем UID как ID
            .set(userData)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
                Toast.makeText(requireContext(), "Регистрация прошла успешно!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
                Toast.makeText(requireContext(), "Ошибка сохранения данных: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
