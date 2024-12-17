package daa.gv.turistenokapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton


class VNFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_v_n, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Находим кнопку после создания View
        val btnDetinets: ImageButton = view.findViewById(R.id.btn_detinets)
        val btnSofiiskii: ImageButton = view.findViewById(R.id.btn_sofiyskii_sobor_vn)
        val btnTysach: ImageButton = view.findViewById(R.id.btn_tysyachiletie_ros)
        val btnTorg: ImageButton = view.findViewById(R.id.btn_yarosl_torg)
        val btnVitosl: ImageButton = view.findViewById(R.id.btn_vitoslavlitsi)

        val btnAkron: ImageButton = view.findViewById(R.id.btn_hotel_akron)
        val btnBianki: ImageButton = view.findViewById(R.id.btn_hotel_bianki)
        val btnVolxov: ImageButton = view.findViewById(R.id.btn_gostinitsa_volxov)
        val btnRachmaninov: ImageButton = view.findViewById(R.id.btn_hotel_raxmaninov)
        val btnRossia: ImageButton = view.findViewById(R.id.btn_hotel_karelinn)

        val btnRestMarus: ImageButton = view.findViewById(R.id.btn_rest_marusya)
        val btnRestPryanik: ImageButton = view.findViewById(R.id.btn_rest_pryanik)
        val btnRestGeogr: ImageButton = view.findViewById(R.id.btn_rest_geogr)
        val btnRestSkazka: ImageButton = view.findViewById(R.id.btn_chaixona_skazka)
        val btnRestTeplo: ImageButton = view.findViewById(R.id.btn_rest_teplo)


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


        // Устанавливаем слушатель кликов для Достопримечательности "Софийский собор"
        btnSofiiskii.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = SofiiskiiSoborFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Достопримечательности "Памятник Тысячелетия России"
        btnTysach.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = TysachiletRosFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Достопримечательности "Торг и Ярославово дворище"
        btnTorg.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = TorgFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Достопримечательности "Витославлицы"
        btnVitosl.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = VitoslavlitsiFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Отеля "Акрон"
        btnAkron.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = HotelAkronFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Отеля "Бианки"
        btnBianki.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = HotelBiankiFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Отеля "Волхов"
        btnVolxov.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = GostVolxovFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Отеля "Рахманинов"
        btnRachmaninov.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = HotelRachmaninovFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Отеля "Россия"
        btnRossia.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = HotelKarelinnFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Ресторана "Маруся"
        btnRestMarus.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = RestMarusyaFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Ресторана "Пряник"
        btnRestPryanik.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = RestPryanikFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Ресторана "География"
        btnRestGeogr.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = RestGeogrFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Ресторана "География"
        btnRestSkazka.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = RestSkazkaFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Ресторана "География"
        btnRestTeplo.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = RestTeploFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }
    }
}