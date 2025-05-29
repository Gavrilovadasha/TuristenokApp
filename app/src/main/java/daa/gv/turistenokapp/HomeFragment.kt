package daa.gv.turistenokapp

import SliderAdapter
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var photos: List<Int>
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var welcomeText: TextView
    private lateinit var avatarButton: ImageButton // Кнопка аватара
    private lateinit var avatarStub: ImageView // Заглушка для аватара

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Инициализация ViewPager
        viewPager = rootView.findViewById(R.id.viewPager)

        // Инициализация Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Находим TextView для приветствия
        welcomeText = rootView.findViewById(R.id.search_input1)

        // Находим кнопку аватара и заглушку
        avatarButton = rootView.findViewById(R.id.avatar_btn)
        avatarStub = rootView.findViewById(R.id.avatar_stub)

        // Проверяем авторизацию пользователя
        val user = auth.currentUser
        if (user != null) {
            // Если пользователь авторизован, показываем аватар
            avatarButton.visibility = View.VISIBLE
            avatarStub.visibility = View.GONE
            // Загружаем сохраненное изображение аватара
            loadImageFromSharedPreferences()
        } else {
            // Если пользователь не авторизован, показываем заглушку
            avatarButton.visibility = View.GONE
            avatarStub.visibility = View.VISIBLE
            welcomeText.visibility = View.GONE
        }

        // Обработка клика по кнопке аватара
        avatarButton.setOnClickListener {
            openProfileFragment()
        }

        // Обработка клика по заглушке
        avatarStub.setOnClickListener {
            Toast.makeText(requireContext(), "Для просмотра профиля необходимо войти в приложение", Toast.LENGTH_SHORT).show()
        }

        // Список изображений (ресурсы drawable)
        photos = listOf(
            R.drawable.vn_slider,
            R.drawable.ladoga_slider,
            R.drawable.pskov_slider,
            R.drawable.rostov_slider
        )

        // Устанавливаем адаптер
        val adapter = SliderAdapter(photos)
        viewPager.adapter = adapter

        // Настраиваем ViewPager2
        setupViewPagerWildberriesStyle()

        // Автоматическое перелистывание
        setupAutoSlider()

        // Настраиваем начальную позицию
        viewPager.setCurrentItem(1, false)

        // Добавляем слушатель состояния авторизации


        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                avatarButton.visibility = View.VISIBLE
                avatarStub.visibility = View.GONE
                welcomeText.visibility = View.VISIBLE
                updateWelcomeMessage()
                // Загружаем аватар заново
                Handler(Looper.getMainLooper()).post {
                    loadImageFromSharedPreferences()
                }
            } else {
                avatarButton.visibility = View.GONE
                avatarStub.visibility = View.VISIBLE
                welcomeText.visibility = View.GONE
            }
        }


        return rootView
    }

    private fun loadImageFromSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString("avatar_uri", null)
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            avatarButton.setImageURI(imageUri) // Устанавливаем изображение
        } else {
            // Если изображение не найдено, устанавливаем дефолтное
            avatarButton.setImageResource(R.drawable.man_avatar)
        }
    }
    override fun onResume() {
        super.onResume()
        if (auth.currentUser != null) {
            loadImageFromSharedPreferences()
        }
    }


    private fun openProfileFragment() {
        val profileFragment = ProfileFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, profileFragment) // Используем контейнер активити
            .addToBackStack(null) // Добавляем транзакцию в back stack
            .commit()
    }

    private fun updateWelcomeMessage() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val userName = document.getString("name") ?: "Пользователь"
                    val greeting = getGreetingByTime(userName)
                    animateText(greeting)
                }
                .addOnFailureListener {
                    val greeting = getGreetingByTime("Пользователь")
                    animateText(greeting)
                }
        } else {
            val greeting = getGreetingByTime(null)
            animateText(greeting)
        }
    }

    private fun getGreetingByTime(userName: String?): String {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            currentHour in 5..11 -> {
                if (userName != null) "Доброе утро, $userName! Готовы к новым открытиям?"
                else "Доброе утро! Готовы к новым открытиям?"
            }
            currentHour in 12..16 -> {
                if (userName != null) "Добрый день, $userName! Куда отправимся сегодня?"
                else "Добрый день! Куда отправимся сегодня?"
            }
            currentHour in 17..22 -> {
                if (userName != null) "Добрый вечер, $userName! Надеемся, вы отлично провели день!"
                else "Добрый вечер! Надеемся, Вы отлично провели день"
            }
            else -> {
                if (userName != null) "Доброй ночи, $userName!"
                else "Доброй ночи!."
            }
        }
    }

    private fun animateText(text: String) {
        val handler = Handler(Looper.getMainLooper())
        val charArray = text.toCharArray()
        var currentIndex = 0
        val runnable = object : Runnable {
            override fun run() {
                if (currentIndex < charArray.size) {
                    // Добавляем по одной букве в TextView
                    welcomeText.append(charArray[currentIndex].toString())
                    currentIndex++
                    // Запускаем следующий шаг через 50 миллисекунд
                    handler.postDelayed(this, 50)
                }
            }
        }
        // Очищаем текст перед началом анимации
        welcomeText.text = ""
        // Запускаем анимацию
        handler.post(runnable)
    }

    private fun setupViewPagerWildberriesStyle() {
        val adapter = SliderAdapter(photos)
        viewPager.adapter = adapter

        // Настраиваем ViewPager2
        viewPager.clipToPadding = false
        viewPager.clipChildren = false

        // Добавляем паддинг для отображения кусочков соседних элементов
        viewPager.setPadding(80, 0, 80, 0) // Отступы слева и справа
        viewPager.offscreenPageLimit = 3 // Загружаем дополнительные элементы

        // Устанавливаем кастомный PageTransformer
        viewPager.setPageTransformer { page, position ->
            val absPos = Math.abs(position)
            // Масштабируем соседние элементы
            page.scaleY = if (absPos > 1) 0.85f else 1f
            page.scaleX = if (absPos > 1) 0.85f else 1f
            // Добавляем прозрачность для дальних элементов
            page.alpha = 0.5f + (1 - absPos) * 0.5f
        }
    }

    private fun setupAutoSlider() {
        runnable = object : Runnable {
            override fun run() {
                var nextItem = viewPager.currentItem + 1
                if (nextItem > photos.size) {
                    nextItem = 1 // Возвращаемся в начало
                }
                viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable!!, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Удаление callback при уничтожении View
        runnable?.let { handler.removeCallbacks(it) }
    }

    private fun openLandmarkDetail(documentId: String) {
        val activity = requireActivity() as? MainActivity ?: return
        activity.openDetailFragment(documentId, "landmark") { id ->
            LandmarkFragment.newInstance(id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обработка кликов по достопримечательностям
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_detinets).setOnClickListener {
            openLandmarkDetail("t0Kd6d9WJ0MTnCFWwMan") // ID достопримечательности
        }
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_kremlin_rostov).setOnClickListener {
            openLandmarkDetail("JzwAylke0OV5LDYHXzzh") // ID достопримечательности
        }
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_troitskii_sobor).setOnClickListener {
            openLandmarkDetail("EFgGd0F3Ta027N0jrHrn") // ID достопримечательности
        }
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_nikolsk_monast_st_ladoga).setOnClickListener {
            openLandmarkDetail("3wlUxMwQv8fva8E2B2zR") // ID достопримечательности
        }
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_gran_palata_vn).setOnClickListener {
            openLandmarkDetail("rCXDAWN2VUIzOvV1zZo2") // ID достопримечательности
        }
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_muzeum_rostov_rupech).setOnClickListener {
            openLandmarkDetail("htF2uCJmTuUTeHZFMEK9") // ID достопримечательности
        }
    }
}