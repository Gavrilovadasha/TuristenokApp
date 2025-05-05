package daa.gv.turistenokapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class HotelsFragment : Fragment(R.layout.fragment_hotels) {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicator: TextView
    private lateinit var mapLinkTextView: TextView
    private lateinit var siteLinkTextView: TextView
    private lateinit var db: FirebaseFirestore
    private var mapUrl: String? = null
    private var siteUrl: String? = null

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

        // Инициализация элементов
        viewPager = view.findViewById(R.id.viewPager)
        indicator = view.findViewById(R.id.indicator)
        mapLinkTextView = view.findViewById(R.id.mapLinkHotel)
        siteLinkTextView = view.findViewById(R.id.siteLinkHotel)

        // Получаем Firestore
        db = FirebaseFirestore.getInstance()

        // Получаем ID достопримечательности из аргументов
        val documentId = arguments?.getString("documentId")

        // Логирование значения landmark_id
        if (documentId != null) {
            Log.d("HotelsFragment", "Received landmark_id: $documentId")
            loadRestData(documentId)
        } else {
            Log.e("HotelsFragment", "landmark_id is null or not provided")
            Toast.makeText(
                requireContext(),
                "ID ресторана не задан",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Настройка клика на ссылку карты
        mapLinkTextView.setOnClickListener {
            if (!mapUrl.isNullOrEmpty()) {
                openMap(mapUrl!!)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ссылка на карту еще загружается",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Настройка клика на ссылку сайта
        siteLinkTextView.setOnClickListener {
            if (!siteUrl.isNullOrEmpty()) {
                openSite(siteUrl!!)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ссылка на сайт еще загружается",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Загрузка данных достопримечательности из Firestore.
     * @param documentId Идентификатор документа.
     */
    private fun loadRestData(documentId: String) {
        db.collection("hotels").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Загрузка имени
                    val name = document.getString("name")
                    view?.findViewById<TextView>(R.id.nameHotel)?.text =
                        name ?: "Имя отсутствует"

                    // Загрузка описания
                    val description = document.getString("description")
                    view?.findViewById<TextView>(R.id.hotelDescription)?.text =
                        description ?: "Описание отсутствует"

                    // Загрузка списка фотографий
                    val img = document.get("img") as? Map<*, *>
                    val imageUrls = mutableListOf<String>()
                    if (!img.isNullOrEmpty()) {
                        for (key in img.keys) {
                            val url = img[key] as? String
                            if (!url.isNullOrEmpty()) {
                                imageUrls.add(url)
                            }
                        }

                        // Настройка ViewPager2
                        if (imageUrls.isNotEmpty()) {
                            val adapter = ImageSliderAdapter(imageUrls)
                            viewPager.adapter = adapter

                            // Настройка индикатора текущей фотографии
                            indicator.text = "1/${imageUrls.size}"
                            viewPager.registerOnPageChangeCallback(object :
                                ViewPager2.OnPageChangeCallback() {
                                override fun onPageSelected(position: Int) {
                                    super.onPageSelected(position)
                                    indicator.text = "${position + 1}/${imageUrls.size}"
                                }
                            })
                        } else {
                            // Если нет фотографий, скрыть ViewPager или показать сообщение
                            viewPager.visibility = View.GONE
                            indicator.visibility = View.GONE
                        }
                    } else {
                        // Если нет фотографий, скрыть ViewPager или показать сообщение
                        viewPager.visibility = View.GONE
                        indicator.visibility = View.GONE
                    }

                    // Загрузка URL карты
                    val maps = document.get("maps") as? Map<*, *>
                    val hotelName = document.getString("name") // Название ресторана
                    mapUrl = maps?.get(hotelName) as? String
                    if (!mapUrl.isNullOrEmpty()) {
                        mapLinkTextView.text = "Открыть карту"
                    } else {
                        mapLinkTextView.text = "Ссылка отсутствует"
                    }

                    // Загрузка URL сайта
                    val site = document.get("site") as? Map<*, *>
                    siteUrl = site?.get(hotelName) as? String

                    if (!siteUrl.isNullOrEmpty()) {
                        siteLinkTextView.text = "Перейти на сайт"
                        siteLinkTextView.movementMethod = LinkMovementMethod.getInstance()
                        siteLinkTextView.isClickable = true

                        siteLinkTextView.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(siteUrl))
                            startActivity(intent)
                        }
                    } else {
                        siteUrl = null
                        siteLinkTextView.text = "Ссылка отсутствует"
                    }
                }
                else {
                    view?.findViewById<TextView>(R.id.hotelDescription)?.text =
                        "Ресторан не найден"
                    mapLinkTextView.text = "Ссылка отсутствует"
                    siteLinkTextView.text = "Ссылка на сайт отсутсвует"
                }
            }
            .addOnFailureListener { exception ->
                view?.findViewById<TextView>(R.id.hotelDescription)?.text =
                    "Ошибка загрузки: ${exception.message}"
                mapLinkTextView.text = "Ошибка загрузки"
            }
    }

    /**
     * Открывает ссылку на карту.
     */
    private fun openMap(url: String) {
        try {
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(requireContext(), "Не удалось открыть карту", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * Открывает ссылку на сайт.
     */
    private fun openSite(site: String) {
        try {
            // Добавляем проверку и преобразование URL
            var url = site
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://$url"
            }

            val siteIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (siteIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(siteIntent)
            } else {
                Toast.makeText(
                    requireContext(), "Не удалось открыть сайт",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(), "Ошибка: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        fun newInstance(documentId: String): HotelsFragment {
            val fragment = HotelsFragment()
            val args = Bundle()
            args.putString("documentId", documentId) // Ключ должен быть "landmark_id"
            fragment.arguments = args
            return fragment
        }
    }
}

