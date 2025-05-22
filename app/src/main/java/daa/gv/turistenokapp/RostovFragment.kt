package daa.gv.turistenokapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class RostovFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rostov, container, false)
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

        // Обработка клика по кнопке для "Ростовский кремль"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_kremlin_rostov).setOnClickListener {
            openLandmarkDetail("JzwAylke0OV5LDYHXzzh") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Успенский собор"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_uspenski_sobor).setOnClickListener {
            openLandmarkDetail("LmYLtuLrf6l3NV0WFrsy") // Укажите ID нужной достопримечательности
        }
        // Обработка клика по кнопке для "Ростовский музей"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_muzeum_rostov_rupech).setOnClickListener {
            openLandmarkDetail("htF2uCJmTuUTeHZFMEK9") // Укажите ID нужной достопримечательности
        }
        // Обработка клика по кнопке для "Музей Царевны-лягушки"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_muzeum_lyagushka).setOnClickListener {
            openLandmarkDetail("NGm2L5BJvgZxBFQVj5sH") // Укажите ID нужной достопримечательности
        }
        // Обработка клика по кнопке для "Митрополичий сад"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_mirtop_sad).setOnClickListener {
            openLandmarkDetail("2HqognBoWLbifedGxZJt") // Укажите ID нужной достопримечательности
        }




        // Обработка клика по кнопке для "Гостиница «Усадьба Плешанова»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_usadba_pleshanova).setOnClickListener {
            openHotelsDetail("q8XXhLcsXxR1B8UIngoC") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отель-ресторан «Рождественский»»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_rozdestvenskii).setOnClickListener {
            openHotelsDetail("MKmRg2MZkxMf26kbuwRM") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отель «Бравис»»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_bravis).setOnClickListener {
            openHotelsDetail("AgSVg0CfsjMCCg81SDJX") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отель «Ростовский»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_rostovskii).setOnClickListener {
            openHotelsDetail("NSfouSwT03pcQQavYlWK") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для Апарт-отель «ЛофтАпарт»
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_loft_appart).setOnClickListener {
            openHotelsDetail("5vYgaRRS6aufu6orWzuK") // Укажите ID нужной достопримечательности
        }





        // Обработка клика по кнопке для "Ресторан «Птичий двор»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_ptich_dvor).setOnClickListener {
            openRestaurantDetail("RPVDUgnr7RFMU4wPbZEn") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Ресторан «Рождественский»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_rozdestvenskii).setOnClickListener {
            openRestaurantDetail("aWOjpTOXyj0XPTtYnIWO") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Ресторан «Русское подворье»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_russ_podvorie).setOnClickListener {
            openRestaurantDetail("aLDDAGHgHM7Hdxnufzko") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Ресторан «Покровские ворота»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_pokr_vorota).setOnClickListener {
            openRestaurantDetail("ZDyoEzxJuGf4PBpjIKvb") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Ресторан «Финифть»"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_finift).setOnClickListener {
            openRestaurantDetail("y7RCXDIP4Vi6jHDa38Lz") // Укажите ID нужной достопримечательности
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