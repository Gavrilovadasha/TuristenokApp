package daa.gv.turistenokapp

import SliderAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView

class RostMuzemKupechFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicator: TextView
    private lateinit var mapLinkTextView: TextView
    private var mapUrl: String? = null // Ссылка на карту из Firestore
    private val images = listOf(
        R.drawable.muzeum_rostov_rupech,
        R.drawable.muzeum_rostov_rupech2,
        R.drawable.muzeum_rostov_rupech3,
        R.drawable.muzeum_rostov_rupech4

        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rost_muzem_kupech, container, false)

        // Настройка ViewPager и индикатора
        viewPager = view.findViewById(R.id.viewPager)
        indicator = view.findViewById(R.id.indicator)
        viewPager.adapter = DetinetsFragment.ImageAdapter(images)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator.text = "${position + 1}/${images.size}"
            }
        })

        // Найти TextView для описания и ссылки
        val descriptionTextView = view.findViewById<TextView>(R.id.landmarkDescription)
        mapLinkTextView = view.findViewById(R.id.mapLinkTextViewRostMuzeum)

        // Загрузка данных для достопримечательности
        loadLandmarkData(
            db = FirebaseFirestore.getInstance(),
            documentId = "htF2uCJmTuUTeHZFMEK9",
            descriptionTextView = descriptionTextView,
            mapLinkTextView = mapLinkTextView
        )

        // Настроить клики на ссылку
        mapLinkTextView.setOnClickListener {
            if (mapUrl != null) {
                openMap(mapUrl!!)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ссылка на карту еще загружается",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }

    // Адаптер для изображений в ViewPager
    class ImageAdapter(private val images: List<Int>) :
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

        // Вложенный класс ViewHolder
        class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView =
                view.findViewById(R.id.imageView) // ID ImageView из вашего макета элемента
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
// Создаем представление для элемента списка
            val view = LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_image,
                    parent,
                    false
                ) // Убедитесь, что у вас есть макет `item_image.xml`
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
// Привязываем изображение из списка `images` к ImageView
            holder.imageView.setImageResource(images[position])
        }

        override fun getItemCount(): Int {
// Возвращаем общее количество элементов
            return images.size
        }
    }
    /**
     * Загрузка данных достопримечательности из Firestore.
     * @param db Экземпляр Firestore.
     * @param documentId Идентификатор документа.
     * @param descriptionTextView TextView для отображения описания.
     * @param mapLinkTextView TextView для отображения ссылки на карту.
     */
    private fun loadLandmarkData(
        db: FirebaseFirestore,
        documentId: String,
        descriptionTextView: TextView,
        mapLinkTextView: TextView
    ) {
        // Загрузка описания
        db.collection("landmarks").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val description = document.getString("description")
                    descriptionTextView.text = description ?: "Описание отсутствует"

                    // Загрузка URL карты
                    val maps = document.get("maps") as? Map<*, *>
                    mapUrl = maps?.get("Музей ростовского купечества") as? String
                    if (!mapUrl.isNullOrEmpty()) {
                        mapLinkTextView.text = "Открыть карту"
                    } else {
                        mapLinkTextView.text = "Ссылка отсутствует"}
                } else {
                    descriptionTextView.text = "Достопримечательность не найдена"
                    mapLinkTextView.text = "Ссылка отсутствует"
                }
            }
            .addOnFailureListener { exception ->
                descriptionTextView.text = "Ошибка загрузки: ${exception.message}"
                mapLinkTextView.text = "Ошибка загрузки"
            }
    }

    /**
     * Открывает ссылку на карте.
     */
    private fun openMap(url: String) {
        try {
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(requireContext(), "Не удалось открыть карту", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}