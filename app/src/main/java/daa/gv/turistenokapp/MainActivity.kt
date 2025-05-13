package daa.gv.turistenokapp

import android.os.Bundle
import android.view.View
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
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )


        // Установка цвета строки состояния
        window.statusBarColor = getColor(R.color.bezh)

        // Добавление флага для темных иконок
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Инициализация Firebase
        auth = FirebaseAuth.getInstance()

        // Инициализация нижней навигации
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Обработчик нажатий на кнопки меню
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            saveSelectedMenuItem(item.itemId) // сохраняем выбранный пункт
            when (item.itemId) {
                R.id.menu_home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.menu_search -> {
                    openFragment(SearchFragment())
                    true
                }
                R.id.menu_routes -> {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        openFragment(SelectRoutesFragment())
                    } else {
                        Toast.makeText(
                            this,
                            "Пожалуйста, авторизируйтесь для доступа к маршрутам",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    true
                }
                R.id.menu_profile -> {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        openFragment(ProfileFragment())
                    } else {
                        openFragment(AvtorizFragment())
                    }
                    true
                }
                else -> false
            }
        }

        // Загрузка последнего выбранного пункта меню
        if (savedInstanceState == null) {
            val selectedItemId = getSavedSelectedMenuItem()
            bottomNavigation.selectedItemId = selectedItemId // автоматически откроет нужный фрагмент
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun saveSelectedMenuItem(itemId: Int) {
        val prefs = getSharedPreferences("main_prefs", MODE_PRIVATE)
        prefs.edit().putInt("selected_menu_item", itemId).apply()
    }

    private fun getSavedSelectedMenuItem(): Int {
        val prefs = getSharedPreferences("main_prefs", MODE_PRIVATE)
        return prefs.getInt("selected_menu_item", R.id.menu_home) // по умолчанию домашняя
    }
}
