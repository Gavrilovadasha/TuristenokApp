package daa.gv.turistenokapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class SuzdalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suzdal, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обработка клика по кнопке "назад"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.back_button).setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            if (fragmentManager.backStackEntryCount > 0) {
                fragmentManager.popBackStack() // Возвращаемся к предыдущему фрагменту
            } else {
                requireActivity().finish() // Закрываем активити, если стек пуст
            }
        }
        // Обработка клика по кнопке для "Суздальский кремль"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_kremlin_suzdal).setOnClickListener {
            openLandmarkDetail("eJIrp1GFVIQisGzuX7Mw") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Музей деревянного зодчества"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_muzuem_derev_zodch).setOnClickListener {
            openLandmarkDetail("ErB3hOQQovenJtGv5Ss7") // Укажите ID нужной достопримечательности
        }
        // Обработка клика по кнопке для "Церковь святых благоверных князей Бориса и Глеба"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_boris_gleb_tserkov).setOnClickListener {
            openLandmarkDetail("z0OKgUk97f5zcqhaqIUx") // Укажите ID нужной достопримечательности
        }
        // Обработка клика по кнопке для "Щурово городище"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_schurovo_gorodische).setOnClickListener {
            openLandmarkDetail("TBFdLiOHhaXa5waKh2Eo") // Укажите ID нужной достопримечательности
        }
        // Обработка клика по кнопке для "Архиерейские палаты "
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_arxiereiskie_palati).setOnClickListener {
            openLandmarkDetail("IOFh0LVD6XZsnzFanv5O") // Укажите ID нужной достопримечательности
        }



        // Обработка клика по кнопке для "Усадьба «Веранда»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_usadba_veranda).setOnClickListener {
            openHotelsDetail("SdBbt6UVlql1CpQDp8Ve") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Антиквар-отель мещанина Охлонина"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_ohlonin).setOnClickListener {
            openHotelsDetail("LF9yZGeZtIPjvHdXEfLf") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отель «Сокол»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_sokol).setOnClickListener {
            openHotelsDetail("WdUUxQzmi5SxfhuKFImH") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Арт-отель «Николаевский Посад»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_nikol_posad).setOnClickListener {
            openHotelsDetail("3H7OIr2I3f3KVEyXfKBD") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Гостиница «Медвежий угол»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_medv_ygol).setOnClickListener {
            openHotelsDetail("CkAQlAaa99ATq8fOjk4a") // Укажите ID нужной достопримечательности
        }





        // Обработка клика по кнопке для Ресторан «Гнездо пекаря»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_gnezdo_pekarya).setOnClickListener {
            openRestaurantDetail("VrAWVM0DPmWFSP0FnXx1") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Ресторан «Гостиный двор»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_gost_dvor).setOnClickListener {
            openRestaurantDetail("iKwubmd4Hojv4jHn8Pgt") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Ресторан «Огурец»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_ogurets).setOnClickListener {
            openRestaurantDetail("LpPtvZyxFb33wssaK6iz") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Ресторан «Лепота»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_lepota).setOnClickListener {
            openRestaurantDetail("2GEVF2rBzApNU8bCx9g6") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Ресторан «ГрафинЪ»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_grafin).setOnClickListener {
            openRestaurantDetail("DcV20ptruAsFgqLOPWJ0") // Укажите ID нужной достопримечательности
        }

    }

    private fun openLandmarkDetail(documentId: String) {
        (requireActivity() as? MainActivity)?.openDetailFragment(
            documentId,
            "LandmarkFragment"
        ) { id -> LandmarkFragment.newInstance(id) }
    }

    private fun openRestaurantDetail(documentId: String) {
        (requireActivity() as? MainActivity)?.openDetailFragment(
            documentId,
            "RestaurantFragment"
        ) { id -> RestaurantFragment.newInstance(id) }
    }

    private fun openHotelsDetail(documentId: String) {
        (requireActivity() as? MainActivity)?.openDetailFragment(
            documentId,
            "HotelsFragment"
        ) { id -> HotelsFragment.newInstance(id) }
    }
}