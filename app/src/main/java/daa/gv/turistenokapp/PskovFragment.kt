package daa.gv.turistenokapp

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment


class PskovFragment : Fragment() {

    private var imgUrl : String? = null // Ссылка на изображения из Firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(daa.gv.turistenokapp.R.layout.fragment_pskov, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обработка клика по кнопке для "Свято-Троицкий кафедральный собор"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_troitskii_sobor).setOnClickListener {
            openLandmarkDetail("EFgGd0F3Ta027N0jrHrn") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Церковь Василия Великого на горке"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_xram_vas_na_gorke).setOnClickListener {
            openLandmarkDetail("NGMxqjuPT5cyWRTQatUH") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Довмонтов город"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_dovmont_city).setOnClickListener {
            openLandmarkDetail("xsCO9Zx4vwOTW5Lw4eac") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Поганкины палаты"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_poginkini_palati).setOnClickListener {
            openLandmarkDetail("BNf62S67fLH1UDCiqA0s") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Изборская крепость"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_izoborsk).setOnClickListener {
            openLandmarkDetail("6lusNppFzMMUMMNfR6BO") // Укажите ID нужной достопримечательности
        }


        // Обработка клика по кнопке для "Отеля Покровский"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_pokrovskii).setOnClickListener {
            openHotelsDetail("esMOeplX21ZBRQYOixG0") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отеля Барселона"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_barselona).setOnClickListener {
            openHotelsDetail("suoekBshRuBjTIOR00RH") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отеля Двор Подзноева"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_dvor_podznoeva).setOnClickListener {
            openHotelsDetail("xZlvETww744yrVaWIw69") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отеля Усадьба Журавлевых"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_usadba).setOnClickListener {
            openHotelsDetail("pusWjaqo7zxb75dePlNK") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отеля Акрон"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_yPokrovki).setOnClickListener {
            openHotelsDetail("UDVo1WTGPQpvHBW7NnKe") // Укажите ID нужной достопримечательности
        }




        // Обработка клика по кнопке для "Ресторан «Покровский»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_pokrovskiy).setOnClickListener {
            openRestaurantDetail("Jjjsedg6nHFSW9Ojg6tB") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Рестобар «Моя история»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_my_history).setOnClickListener {
            openRestaurantDetail("iQAwBSKGG0KAGimvZqyy") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Ресторан Самовар
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_samovar).setOnClickListener {
            openRestaurantDetail("8GK4zyRYIRXv0pPR7VFf") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Таверна «Пожарка»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_pozarka).setOnClickListener {
            openRestaurantDetail("TjcYhrVnEVngz7iNatJy") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Ресторан «Примостье»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_primostie).setOnClickListener {
            openRestaurantDetail("RY5BEYvZ70tY7FneAURP") // Укажите ID нужной достопримечательности
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