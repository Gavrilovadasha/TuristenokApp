package daa.gv.turistenokapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инфлейтим макет фрагмента
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Находим кнопку после создания View
        val btnVN : ImageButton = view.findViewById(R.id.VelikiyNovgorod)
        val btnPskov : ImageButton = view.findViewById(R.id.Pskov)
        val btnLadoga : ImageButton = view.findViewById(R.id.SrarayaLadoga)
        val btnRostov : ImageButton = view.findViewById(R.id.Rostov)



        // Устанавливаем слушатель кликов для Великого Новгорода
        btnVN.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = VNFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Пскова
        btnPskov.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = PskovFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Страрой Ладоги
        btnLadoga.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = StarayaLadogaFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }

        // Устанавливаем слушатель кликов для Ростова
        btnRostov.setOnClickListener {
            // Создаём новый фрагмент
            val fragment = RostovFragment()

            // Переходим на новый фрагмент
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // Добавляем в BackStack, чтобы можно было вернуться назад
            transaction.commit()
        }
    }
}
