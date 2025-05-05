package daa.gv.turistenokapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class StarayaLadogaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staraya_ladoga, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обработка клика по кнопке для "Староладожская крепость"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_ladoga_krepost).setOnClickListener {
            openLandmarkDetail("7YzWqQ5KAvOtVUkzWPTB") // Укажите ID нужной достопримечательности
        }
        // Обработка клика по кнопке для "Никольский монастырь"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_nikolsk_monast_st_ladoga).setOnClickListener {
            openLandmarkDetail("3wlUxMwQv8fva8E2B2zR") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Дом П.В.Калязина"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_home_Kalyazin).setOnClickListener {
            openLandmarkDetail("Nk4dWi7ZweCU5Ldc2pPB") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Георгиевская церковь"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_georg_xram).setOnClickListener {
            openLandmarkDetail("bnPnGQro0lGj8KANuowf") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Курган"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_kurgan).setOnClickListener {
            openLandmarkDetail("09reV4jwXtxrJo1eKY1r") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Гостиница «Староладожская дача»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_st_lad_dacha).setOnClickListener {
            openHotelsDetail("2YoatUx9eE71rXOaemux") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отель «Барский»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_barskii).setOnClickListener {
            openHotelsDetail("kZz1v3LqjrEn2Vt7fgZq") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Арт-коттедж «Староладожская усадьба»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_st_lad_usadba).setOnClickListener {
            openHotelsDetail("msuMNxDrTYDgbOQsVV25") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Гостевой дом «Стрековец»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_strekovets).setOnClickListener {
            openHotelsDetail("F2BlkaPz3ZbeMH8rvVPu") // Укажите ID нужной достопримечательности
        }


        // Обработка клика по кнопке для Кафе «Древняя Ладога»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_drevn_ladoga).setOnClickListener {
            openRestaurantDetail("KTAoBvYTdeaNKHWSVzbM") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Ресторан «Князь Рюрик»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_knyaz_rurik).setOnClickListener {
            openRestaurantDetail("dxrQNfUPs1flBDbLKR77") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Ресторан «Староладожская дача»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_st_lad_dacha).setOnClickListener {
            openRestaurantDetail("C3NllwNiGgkIAUaC9XG6") // Укажите ID нужной достопримечательности
        }

    }

    private fun openLandmarkDetail(documentId: String) {
        val landmarkFragment = LandmarkFragment.newInstance(documentId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(daa.gv.turistenokapp.R.id.container, landmarkFragment) // Используем контейнер активити
            .addToBackStack(null)
            .commit()
    }
    private fun openHotelsDetail(documentId: String) {
        val hotelsFragment = HotelsFragment.newInstance(documentId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(daa.gv.turistenokapp.R.id.container, hotelsFragment) // Используем контейнер активити
            .addToBackStack(null)
            .commit()
    }

    private fun openRestaurantDetail(documentId: String) {
        val restaurantFragment = RestaurantFragment.newInstance(documentId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(daa.gv.turistenokapp.R.id.container, restaurantFragment) // Используем контейнер активити
            .addToBackStack(null)
            .commit()
    }
}