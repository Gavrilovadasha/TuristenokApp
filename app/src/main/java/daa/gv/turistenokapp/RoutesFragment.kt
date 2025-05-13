package daa.gv.turistenokapp

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class RoutesFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private var imageUrls: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(daa.gv.turistenokapp.R.layout.fragment_routes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFirestore()
        loadRouteData(arguments?.getString("documentId"))

        // Находим кнопку img_btn_routes
        val imgBtnRoutes = view.findViewById<ImageButton>(R.id.img_btn_routes)

        // Устанавливаем обработчик клика
        imgBtnRoutes.setOnClickListener {
            val routeId = arguments?.getString("documentId")
            if (routeId != null) {
                loadRouteDescription(routeId)
            } else {
                showToast("Ошибка: ID маршрута не найден")
            }
        }

        val typeWriterText = view.findViewById<TextView>(R.id.textRoutes)
        val fullText = "Узнайте больше о маршруте!"
        var index = 0
        val delay: Long = 80 // задержка между буквами

        val handler = android.os.Handler()
        val runnable = object : Runnable {
            override fun run() {
                if (index <= fullText.length) {
                    typeWriterText.text = fullText.substring(0, index)
                    index++
                    handler.postDelayed(this, delay)
                }
            }
        }
        handler.post(runnable)

    }

    private fun setupFirestore() {
        db = FirebaseFirestore.getInstance()
    }

    private fun loadRouteData(routeId: String?) {
        routeId?.let { id ->
            db.collection("routesList").document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Загружаем изображения
                        val images = (document.get("img") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                        imageUrls = images

                        // Загружаем идентификаторы достопримечательностей
                        val landmarksId = (document.get("landmarks") as? List<*>)?.filterIsInstance<String>() ?: emptyList()

                        // Передаем данные в аргументы фрагмента
                        arguments?.putStringArrayList("landmarks", ArrayList(landmarksId))

                        // Устанавливаем кнопки с изображениями
                        setupImageButtons(images)

                    } else {
                        showToast("Маршрут не найден")
                    }
                }
                .addOnFailureListener { e ->
                    showToast("Ошибка загрузки: ${e.message}")
                }
        } ?: showToast("Ошибка: ID маршрута не найден")
    }

    private fun loadRouteDescription(routeId: String) {
        db.collection("routesList").document(routeId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Загружаем описание маршрута из поля descriptionRoute
                    val nameRoute = document.getString("nameRoute") ?: "Название маршрута отсутствует"
                    val description = document.getString("descriptionRoute") ?: "Описание маршрута отсутствует"

                    showRouteInfoDialog(nameRoute, description)
                } else {
                    showToast("Маршрут не найден")
                }
            }
            .addOnFailureListener { e ->
                showToast("Ошибка загрузки: ${e.message}")
            }
    }

    private fun setupImageButtons(imageUrls: List<String>) {
        if (imageUrls.isEmpty()) {
            showToast("Нет изображений для маршрута")
            return
        }

        val imageButtons = listOf(
            view?.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.land1),
            view?.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.land2),
            view?.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.land3),
            view?.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.land4),
            view?.findViewById<ImageButton>(daa.gv.turistenokapp.R.id.land5),
        )

        // Загружаем изображения в ImageButton
        for (i in imageUrls.indices) {
            val imageUrl = imageUrls[i]
            val imageButton = imageButtons[i] ?: continue

            Glide.with(this)
                .load(imageUrl)
                .circleCrop() // Преобразование изображения в круг
                .into(imageButton)

            // Обработка клика по ImageButton
            imageButton.setOnClickListener {
                openLandmarkDetail(i) // Передаем индекс изображения
            }
        }
    }

    private fun openLandmarkDetail(index: Int) {
        // Проверяем, есть ли данные о достопримечательностях
        val landmarkDocumentIds = arguments?.getStringArrayList("landmarks") ?: emptyList()
        if (index >= landmarkDocumentIds.size) {
            showToast("Ошибка: индекс достопримечательности некорректен")
            return
        }
        // Получаем documentId соответствующей достопримечательности
        val landmarkId = landmarkDocumentIds[index]

        // Создаем новый фрагмент для достопримечательности
        val landmarkFragment = LandmarkFragment.newInstance(landmarkId)

        // Заменяем текущий фрагмент на фрагмент достопримечательности
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(daa.gv.turistenokapp.R.id.container, landmarkFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showRouteInfoDialog(routeName: String, routeDescription: String) {
        // Создаем диалог
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_route_info)

        // Находим элементы интерфейса
        val dialogName = dialog.findViewById<TextView>(R.id.dialog_name_route)
        val dialogText = dialog.findViewById<TextView>(R.id.dialog_text)
        val closeButton = dialog.findViewById<Button>(R.id.dialog_close_button)

        dialogName.text = routeName

        // Устанавливаем текст в диалог
        dialogText.text = Html.fromHtml(routeDescription, Html.FROM_HTML_MODE_COMPACT)

        // Добавляем анимацию
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        // Обработчик закрытия диалога
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        // Показываем диалог
        dialog.show()
    }

    companion object {
        fun newInstance(documentId: String, landmarksId: List<String>) = RoutesFragment().apply {
            arguments = Bundle().apply {
                putString("documentId", documentId)
                putStringArrayList("landmarks", ArrayList(landmarksId))
            }
        }
    }
}