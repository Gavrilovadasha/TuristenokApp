package daa.gv.turistenokapp

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var bottomNavigation: BottomNavigationView

    // Фрагменты
    private var homeFragment = HomeFragment()
    private var searchFragment = SearchFragment()
    private var routesFragment = SelectRoutesFragment()
    private var profileFragment = ProfileFragment()
    private var avtorizFragment = AvtorizFragment()


    private lateinit var currentFragment: Fragment

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.statusBarColor = getColor(R.color.bezh)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        auth = FirebaseAuth.getInstance()
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Загрузка сохраненного состояния или первоначальное открытие
        if (savedInstanceState == null) {
            val selectedItemId = getSavedSelectedMenuItem()
            loadFragments()
            bottomNavigation.selectedItemId = selectedItemId
        } else {
            // Восстановление текущего фрагмента из менеджера
            currentFragment = supportFragmentManager.findFragmentById(R.id.container) ?: homeFragment
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            if (fragment is SearchFragment) {
                fragment.refreshUI()
            }

            // Обновляем currentFragment, чтобы show/hide продолжали работать
            if (fragment != null) {
                currentFragment = fragment
            }
        }

        setupBottomNavigation()
    }


    private fun loadFragments() {
        val manager = supportFragmentManager

        // Попытка получить уже существующие фрагменты по тегам
        val home = manager.findFragmentByTag("home") ?: homeFragment
        val search = manager.findFragmentByTag("search") ?: searchFragment
        val routes = manager.findFragmentByTag("routes") ?: routesFragment
        val profile = manager.findFragmentByTag("profile") ?: profileFragment
        val avtoriz = manager.findFragmentByTag("avtoriz") ?: avtorizFragment

        val transaction = manager.beginTransaction()

        // Если фрагменты не были добавлены, добавляем их
        if (!home.isAdded) transaction.add(R.id.container, home, "home")
        if (!search.isAdded) transaction.add(R.id.container, search, "search")
        if (!routes.isAdded) transaction.add(R.id.container, routes, "routes")
        if (!profile.isAdded) transaction.add(R.id.container, profile, "profile")
        if (!avtoriz.isAdded) transaction.add(R.id.container, avtoriz, "avtoriz")

        // Скрываем все кроме домашнего
        transaction.hide(search)
        transaction.hide(routes)
        transaction.hide(profile)
        transaction.hide(avtoriz)
        transaction.show(home)

        transaction.commit()

        // Обновляем ссылки в классе
        homeFragment = home as HomeFragment
        searchFragment = search as SearchFragment
        routesFragment = routes as SelectRoutesFragment
        profileFragment = profile as ProfileFragment
        avtorizFragment = avtoriz as AvtorizFragment

        currentFragment = homeFragment
    }


    private fun setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            saveSelectedMenuItem(item.itemId)
            when (item.itemId) {
                R.id.menu_home -> showFragment(homeFragment)
                R.id.menu_search -> showFragment(searchFragment)
                R.id.menu_routes -> {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        showFragment(routesFragment)
                    } else {
                        Toast.makeText(this, "Пожалуйста, авторизируйтесь для доступа к маршрутам", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.menu_profile -> {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        showFragment(profileFragment)
                    } else {
                        showFragment(avtorizFragment)
                    }
                }
            }
            true
        }
    }

    private fun showFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        // Скрываем все фрагменты кроме нужного
        manager.fragments.forEach {
            if (it == fragment) {
                transaction.show(it)
            } else {
                transaction.hide(it)
            }
        }

        transaction.commit()
        currentFragment = fragment
    }

    fun switchToCityFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        // Скрываем текущий фрагмент
        transaction.hide(currentFragment)

        // Проверяем, добавлен ли фрагмент уже (например, чтобы не дублировать)
        val tag = fragment::class.java.simpleName
        val existing = supportFragmentManager.findFragmentByTag(tag)

        if (existing == null) {
            // Добавляем новый фрагмент и сохраняем в back stack
            transaction.add(R.id.container, fragment, tag)
            transaction.addToBackStack(tag)
        } else {
            // Если уже добавлен, просто показываем
            transaction.show(existing)
            transaction.addToBackStack(tag)
        }

        transaction.commit()

        currentFragment = fragment
    }
    fun <T : Fragment> openDetailFragment(
        documentId: String,
        fragmentTagPrefix: String,
        createFragment: (String) -> T
    ) {
        val tag = "${fragmentTagPrefix}_$documentId"
        val existing = supportFragmentManager.findFragmentByTag(tag)

        val transaction = supportFragmentManager.beginTransaction()

        // Скрываем все видимые фрагменты
        supportFragmentManager.fragments.forEach { fragment ->
            if (fragment.isVisible) {
                transaction.hide(fragment)
            }
        }

        val fragment = existing ?: createFragment(documentId)
        if (existing == null) {
            transaction.add(R.id.container, fragment, tag)
        } else {
            transaction.show(fragment)
        }

        transaction.addToBackStack(tag)
        transaction.commit()

        currentFragment = fragment
    }

    private fun saveSelectedMenuItem(itemId: Int) {
        val prefs = getSharedPreferences("main_prefs", MODE_PRIVATE)
        prefs.edit().putInt("selected_menu_item", itemId).apply()
    }

    private fun getSavedSelectedMenuItem(): Int {
        val prefs = getSharedPreferences("main_prefs", MODE_PRIVATE)
        return prefs.getInt("selected_menu_item", R.id.menu_home)
    }
}
