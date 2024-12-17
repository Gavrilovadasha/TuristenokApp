package daa.gv.turistenokapp

import SliderAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var photos: List<Int>
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val favoritePlaces = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инициализация корневого View
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Найдите ViewPager2
        viewPager = rootView.findViewById(R.id.viewPager)


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

        // Настраиваем начальную позицию
        viewPager.setCurrentItem(1, false) // начинаем со второго элемента (т.к. первый элемент в адаптере обёрнут)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // Проверяем, достигли ли мы начала/конца списка и корректируем позицию
                if (position == 0) {
                    viewPager.setCurrentItem(photos.size, false) // Перемещаемся в конец
                } else if (position == photos.size + 1) {
                    viewPager.setCurrentItem(1, false) // Перемещаемся в начало
                }
            }
        })

        setupAutoSlider()
        setupViewPagerWildberriesStyle()

        return rootView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Переход на достопримечательность "Детинец"
        val btnDetinets: ImageButton = view.findViewById(R.id.btn_detinets)

        // Устанавливаем слушатель кликов для Достопримечательности "Новгородский Детинец"
        btnDetinets.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = DetinetsFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Переход на достопримечательность "Ростовский Кремль"
        val btnRostKremlin: ImageButton = view.findViewById(R.id.btn_kremlin_rostov)

        // Устанавливаем слушатель кликов для Достопримечательности "Новгородский Детинец"
        btnRostKremlin.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = RostovKremlinFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Переход на достопримечательность "Псковский Свято-Троицкий собор"
        val btnSvyatoTroiysPskov: ImageButton = view.findViewById(R.id.btn_troitskii_sobor)

        // Устанавливаем слушатель кликов для Достопримечательности "Новгородский Детинец"
        btnSvyatoTroiysPskov.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = PskovTroitSoborFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }


        // Переход на достопримечательность "Никольский мужской монастырь"
        val btnNikolsMonas: ImageButton = view.findViewById(R.id.btn_nikolsk_monast_st_ladoga)

        // Устанавливаем слушатель кликов для Достопримечательности "Новгородский Детинец"
        btnNikolsMonas.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = LadogaNikolsMonasFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Переход на достопримечательность "Никольский мужской монастырь"
        val btnGranPalataVn: ImageButton = view.findViewById(R.id.btn_gran_palata_vn)

        // Устанавливаем слушатель кликов для Достопримечательности "Новгородский Детинец"
        btnGranPalataVn.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = VnGranPalataFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Переход на достопримечательность "Никольский мужской монастырь"
        val btnGranRostMuseum: ImageButton = view.findViewById(R.id.btn_muzeum_rostov_rupech)

        // Устанавливаем слушатель кликов для Достопримечательности "Новгородский Детинец"
        btnGranRostMuseum.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = RostMuzemKupechFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }
    }
}