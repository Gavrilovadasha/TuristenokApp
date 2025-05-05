package daa.gv.turistenokapp

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class SelectRoutesFragment : Fragment() {

    private val routeLandmarkMap = mapOf(
        "HKfL2WQXbofhyroQ6Xtc" to listOf("landmark1", "landmark2", "landmark3"),
        "SaWwvYH3vMtyAzKZNxYL" to listOf("landmark4", "landmark5"),
        "vgY4ncjUxDplnq39smpD" to listOf("landmark6", "landmark7", "landmark8")
    )

    private val routeIdMap = mapOf(
        "Маршрут через века: от Софии до Волхова" to "HKfL2WQXbofhyroQ6Xtc",
        "Сердце Пскова: путешествие сквозь время" to "SaWwvYH3vMtyAzKZNxYL",
        "Староладожские хроники: путешествие в колыбель Руси" to "vgY4ncjUxDplnq39smpD",
        "Ростовская сага: два дня в городе золотых куполов" to "V49hZklnOB0I8r0RtMjl",
        "Три дня в Суздале: где время останавливается" to "XYYUGBsdTN9PFFQz77QD"

    )

    private lateinit var cities_container: LinearLayout
    private val citiesData = mapOf(
        "Маршрут через века: от Софии до Волхова" to R.drawable.routes_btn,
        "Сердце Пскова: путешествие сквозь время" to R.drawable.routes_pskov,
        "Староладожские хроники: путешествие в колыбель Руси" to R.drawable.routes_ladoga,
        "Ростовская сага: два дня в городе золотых куполов" to R.drawable.routes_rostov,
        "Три дня в Суздале: где время останавливается" to R.drawable.routes_suzdal,
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_routes, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cities_container = view.findViewById(R.id.cities_container)
        val searchInput = view.findViewById<EditText>(R.id.search_input2)

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCities(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        filterCities("")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterCities(query: String) {
        cities_container.removeAllViews()
        val filteredQuery = query.toLowerCase().trim()

        citiesData.forEach { (cityName, cityImageRes) ->
            if (filteredQuery.isEmpty() || cityName.toLowerCase().contains(filteredQuery)) {
                addCityToContainer(cityName, cityImageRes)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCityToContainer(cityName: String, cityImageRes: Int) {
        // Контейнер для элемента маршрута
        val frameLayout = FrameLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8.dpToPx(), 0, 8.dpToPx())
            }
        }

        // Адаптивная кнопка с изображением
        val imageButton = ImageButton(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                (resources.displayMetrics.widthPixels * 0.92).toInt(), // 92% ширины экрана
                (resources.displayMetrics.widthPixels * 0.4).toInt()   // 40% ширины экрана
            ).apply {
                gravity = android.view.Gravity.CENTER_HORIZONTAL
                setMargins(0, 8.dpToPx(), 0, 8.dpToPx())
            }
            setBackgroundResource(cityImageRes)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setOnClickListener {
                routeIdMap[cityName]?.let { openRouteDetail(it) }
            }
        }

        // Текст с адаптивными параметрами
        val textView = TextView(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.START or android.view.Gravity.TOP
                setMargins(
                    15.dpToPx(), // 24dp
                    45.dpToPx(),
                    10,
                    0
                )
            }
            text = cityName.replace("  ", "\n")
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 32f)
            setTypeface(resources.getFont(R.font.font))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.bezh))        }

        frameLayout.addView(imageButton)
        frameLayout.addView(textView)
        cities_container.addView(frameLayout)
    }

    private fun openRouteDetail(documentId: String) {
        val landmarkIds = routeLandmarkMap[documentId] ?: emptyList()
        val routesFragment = RoutesFragment.newInstance(documentId, landmarkIds)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, routesFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density + 0.5f).toInt()
    }
}