package daa.gv.turistenokapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AvtorizFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_avtoriz, container, false)

        // Инициализация Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()



        val regTxt : TextView = view.findViewById(R.id.txt_sign_up)
        regTxt.setOnClickListener {
            val fragment = RegFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container,fragment)?.commit()
        }


        val emailInput = view.findViewById<EditText>(R.id.edit_email_avt)
        val passwordAvt = view.findViewById<EditText>(R.id.edit_pass_avt)
        val buttonSignIn = view.findViewById<Button>(R.id.btn_avt)

        buttonSignIn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordAvt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            if (!email.contains("@")) {
                Toast.makeText(requireContext(), "Некорректный email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Вход через Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                            // Создаём новый фрагмент
                            val fragment = ProfileFragment()
                            // Переходим на новый фрагмент
                            val transaction = parentFragmentManager.beginTransaction()
                            transaction.replace(R.id.container, fragment)
                            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
                            transaction.commit()


                        // Авторизация успешна, переход на профиль
                        Toast.makeText(requireContext(), "Добро пожаловать!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Ошибка авторизации
                        Toast.makeText(requireContext(), "Ошибка авторизации: Вы ввели неверный логин или пароль", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        return view
    }
}
