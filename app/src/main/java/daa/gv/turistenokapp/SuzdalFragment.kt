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
    }

    private fun openLandmarkDetail(documentId: String) {
        val landmarkFragment = LandmarkFragment.newInstance(documentId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(daa.gv.turistenokapp.R.id.container, landmarkFragment) // Используем контейнер активити
            .addToBackStack(null)
            .commit()
    }
}