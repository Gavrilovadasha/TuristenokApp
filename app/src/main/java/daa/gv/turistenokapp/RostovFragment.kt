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
    }

    private fun openLandmarkDetail(documentId: String) {
        val landmarkFragment = LandmarkFragment.newInstance(documentId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(daa.gv.turistenokapp.R.id.container, landmarkFragment) // Используем контейнер активити
            .addToBackStack(null)
            .commit()
    }
}