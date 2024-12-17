package daa.gv.turistenokapp

import android.annotation.SuppressLint
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
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView

class RestTeploFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicator: TextView
    private lateinit var mapLinkTextView: TextView
    private lateinit var siteLinkTextView: TextView
    private var mapUrl: String? = null // Ссылка на карту из Firestore
    private var siteUrl: String? = null // Ссылка на сайт из Firestore
    private val images = listOf(
        R.drawable.rest_teplo1,
        R.drawable.rest_teplo2,
        R.drawable.rest_teplo3,
        R.drawable.rest_teplo4,
        R.drawable.rest_teplo5,
        R.drawable.rest_teplo6
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rest_teplo, container, false)

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
        val descriptionTextView = view.findViewById<TextView>(R.id.hotelsDescription)
        mapLinkTextView = view.findViewById(R.id.mapLinkRestTeplo)
        siteLinkTextView = view.findViewById(R.id.siteLinkRestTeplo)

        // Загрузка данных для достопримечательности
        loadLandmarkData(
            db = FirebaseFirestore.getInstance(),
            documentId = "7SXs35byKvjw3Q4P4Th6",
            descriptionTextView = descriptionTextView,
            mapLinkTextView = mapLinkTextView,
            siteLinkTextView = siteLinkTextView
        )

        // Настроить клики на ссылку карты
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

        // Настроить клики на ссылку сайта
        siteLinkTextView.setOnClickListener {
            if (siteUrl != null) {
                val uri = Uri.parse(siteUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Ссылка на сайт еще загружается", Toast.LENGTH_SHORT).show()
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
        mapLinkTextView: TextView,
        siteLinkTextView: TextView
    ) {
        db.collection("restaurant").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val description = document.getString("description")
                    descriptionTextView.text = description ?: "Описание отсутствует"

                    val maps = document.get("maps") as? Map<*, *>
                    mapUrl = maps?.get("Траттория Тепло") as? String
                    if (!mapUrl.isNullOrEmpty()) {
                        mapLinkTextView.text = "Открыть карту"
                    } else {
                        mapLinkTextView.text = "Ссылка отсутствует"
                    }

                    val site = document.getString("site")
                    if (!site.isNullOrEmpty()) {
                        siteLinkTextView.text = "Перейти на сайт"
                        siteUrl = site
                    } else {
                        siteLinkTextView.text = "Ссылка не найдена"
                        siteUrl = null
                    }
                } else {
                    descriptionTextView.text = "Достопримечательность не найдена"
                    mapLinkTextView.text = "Ссылка отсутствует"
                    siteLinkTextView.text = "Ссылка на сайт отсутствует"
                }
            }
            .addOnFailureListener { exception ->
                descriptionTextView.text = "Ошибка загрузки: ${exception.message}"
                mapLinkTextView.text = "Ошибка загрузки карты"
                siteLinkTextView.text = "Ошибка загрузки сайта"
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
                Toast.makeText(requireContext(), "Не удалось открыть", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Открывает ссылку на сайт.
    private fun openWebSite(url: String) {
        try {
            val siteIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (siteIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(siteIntent)
            } else {
                Toast.makeText(requireContext(), "Не удалось открыть сайт", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


}
