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
import androidx.lifecycle.ViewModel



class VNFragment : Fragment() {

    private var imgUrl : String? = null // Ссылка на изображения из Firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(daa.gv.turistenokapp.R.layout.fragment_v_n, container, false)
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

        // Обработка клика по кнопке для "Детинец"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_detinets).setOnClickListener {
            openLandmarkDetail("t0Kd6d9WJ0MTnCFWwMan") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Софийский собор"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_sofiyskii_sobor_vn).setOnClickListener {
            openLandmarkDetail("SCtup04YtHJW93m0hvzq") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Памятник тысячелетию России"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_tysyachiletie_ros).setOnClickListener {
            openLandmarkDetail("ndkvnI9tUJWOqiv02fvQ") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Торг и Ярославово дворище"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_yarosl_torg).setOnClickListener {
            openLandmarkDetail("pxReg22dOWbJ6ljIwIt4") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Витославлицы"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_vitoslavlitsi).setOnClickListener {
            openLandmarkDetail("S3GEkjGgcv9AaeU1iYcN") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Ресторана пряник"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_pryanik).setOnClickListener {
            openRestaurantDetail("0H3rgSTOjttp45aQy88P") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Ресторана География"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_geogr).setOnClickListener {
            openRestaurantDetail("1r9gTOoucXqs9c3vTOJF") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Ресторана Тепло"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_teplo).setOnClickListener {
            openRestaurantDetail("7SXs35byKvjw3Q4P4Th6") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Ресторана Маруся"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_rest_marusya).setOnClickListener {
            openRestaurantDetail("pBLRFCg25zfDx0oPSkSK") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Чайхана Сказка"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_chaixona_skazka).setOnClickListener {
            openRestaurantDetail("xYGfl4oYY5yrUThyuva9") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отеля Акрон"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_akron).setOnClickListener {
            openHotelsDetail("kY1KkDQ0DSpnNs6shTpR") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отеля Бианки"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_bianki).setOnClickListener {
            openHotelsDetail("wrhNgd3jeDyDmw4IhPJ0") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отеля Волхов"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_gostinitsa_volxov).setOnClickListener {
            openHotelsDetail("wBFWXF0pBwZfKyy8MHOu") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отеля Рахманинов"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_raxmaninov).setOnClickListener {
            openHotelsDetail("R0oFsEDPDX3N3qaTiMN6") // Укажите ID нужной достопримечательности
        }

        // Обработка клика по кнопке для "Отеля Карелинн"
        view.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.btn_hotel_karelinn).setOnClickListener {
            openHotelsDetail("AI23fasUg7kEFPKK6cQ4") // Укажите ID нужной достопримечательности
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