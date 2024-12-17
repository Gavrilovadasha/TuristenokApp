package daa.gv.turistenokapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация Firebase
        auth = FirebaseAuth.getInstance()

        // Инициализация нижней навигации
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Открытие HomeFragment при запуске
        if (savedInstanceState == null) { // Проверка, чтобы не пересоздавать фрагмент при повороте экрана
            openFragment(HomeFragment())
        }

        // Обработчик нажатий на кнопки меню
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    // Открытие главного фрагмента
                    openFragment(HomeFragment())
                    true
                }
                R.id.menu_search -> {
                    // Открытие фрагмента поиска
                    openFragment(SearchFragment())
                    true
                }
                R.id.menu_routes -> {
                    // Проверяем наличие текущего пользователя
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        openFragment(RoutesFragment())
                    } else {
                        Toast.makeText(this, "Пожалуйста, авторизируйтесь для доступа к маршрутам", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                R.id.menu_profile -> {
                    // Проверка на авторизацию пользователя
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        // Если пользователь авторизован, открываем профиль
                        openFragment(ProfileFragment())
                    } else {
                        // Если пользователь не авторизован, открываем авторизацию
                        openFragment(AvtorizFragment())
                    }
                    true
                }
                else -> false
            }
        }

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null) // Добавляем в back stack, чтобы можно было вернуться назад
        transaction.commit()
    }
}


