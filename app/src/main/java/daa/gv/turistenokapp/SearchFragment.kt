package daa.gv.turistenokapp

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class SearchFragment : Fragment() {

    private lateinit var cities_container: LinearLayout // Изменяем тип на LinearLayout
    private val citiesData = mapOf(
        "Великий Новгород" to R.drawable.vn,
        "Псков" to R.drawable.pskov,
        "Старая Ладога" to R.drawable.ladoga,
        "Ростов Великий" to R.drawable.rostov,
        "Суздаль" to R.drawable.suzdal,
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noResultsText = view.findViewById<TextView>(R.id.no_results_text)

        // Находим контейнер для отображения городов
        cities_container = view.findViewById(R.id.cities_container) // Теперь это LinearLayout

        // Находим поле поиска
        val searchInput = view.findViewById<EditText>(R.id.search_input2)

        // Добавляем слушатель для изменения текста
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCities(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Инициализируем контейнер при первом запуске
        filterCities("") // Отображаем все города
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterCities(query: String) {
        cities_container.removeAllViews()

        val filteredQuery = query.toLowerCase().trim()
        val filteredCities = if (filteredQuery.isEmpty()) {
            citiesData.toList()
        } else {
            citiesData.filter { (cityName, _) ->
                cityName.toLowerCase().contains(filteredQuery)
            }.toList()
        }

        val noResultsText = view?.findViewById<TextView>(R.id.no_results_text)
        if (filteredCities.isEmpty()) {
            noResultsText?.visibility = View.VISIBLE
        } else {
            noResultsText?.visibility = View.GONE
            filteredCities.forEach { (cityName, cityImageRes) ->
                addCityToContainer(cityName, cityImageRes)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCityToContainer(cityName: String, cityImageRes: Int) {
        // Контейнер для города
        val cityFrameLayout = FrameLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8.dpToPx(), 0, 8.dpToPx())
            }
        }

        // Кнопка с изображением - измененные параметры
        val imageButton = ImageButton(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                (resources.displayMetrics.widthPixels * 0.92).toInt(), // 92% ширины экрана
                (resources.displayMetrics.widthPixels * 0.4).toInt() // сохраняем высоту
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                setMargins(0, 8.dpToPx(), 0, 8.dpToPx()) // убрали боковые отступы
            }
            setBackgroundResource(cityImageRes)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setOnClickListener { navigateToCityFragment(getCityFragment(cityName)) }
        }

        // Текст поверх изображения (скорректированные отступы)
        val textView = TextView(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.START or Gravity.TOP
                setMargins(
                    15.dpToPx(), // увеличенный отступ слева
                    40.dpToPx(),
                    10,
                    0
                )
            }
            text = cityName.replace(" ", "\n")
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 50f)
            setTypeface(resources.getFont(R.font.font))
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        }

        cityFrameLayout.addView(imageButton)
        cityFrameLayout.addView(textView)
        cities_container.addView(cityFrameLayout)
    }
    private fun getCityFragment(cityName: String): Fragment {
        return when (cityName) {
            "Великий Новгород" -> VNFragment()
            "Псков" -> PskovFragment()
            "Старая Ладога" -> StarayaLadogaFragment()
            "Ростов Великий" -> RostovFragment()
            "Суздаль" -> SuzdalFragment()
            else -> throw IllegalArgumentException("Unknown city")
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshUI() {
        val searchInput = view?.findViewById<EditText>(R.id.search_input2)
        val query = searchInput?.text.toString()
        filterCities(query)
    }
    private fun navigateToCityFragment(fragment: Fragment) {
        (requireActivity() as MainActivity).switchToCityFragment(fragment)
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}