package daa.gv.turistenokapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.firestore.ServerTimestamp



class LandmarkFragment : Fragment(R.layout.fragment_landmark) {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicator: TextView
    private lateinit var mapLinkTextView: TextView
    private lateinit var db: FirebaseFirestore
    private var mapUrl: String? = null
    private var imageUrls: List<String> = emptyList()
    private lateinit var ratingBar: RatingBar
    private lateinit var commentEditText: EditText
    private lateinit var submitReviewButton: Button
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var averageRatingTextView: TextView
    private var isReviewsVisible = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupFirestore()
        setupClickListeners()
        loadData()

        val toggleButton = view.findViewById<TextView>(R.id.toggleReviewsButton)
        val arrowIcon = view.findViewById<ImageView>(R.id.arrowIcon)

        toggleButton.setOnClickListener {
            isReviewsVisible = !isReviewsVisible
            toggleReviewsVisibility(reviewsRecyclerView, arrowIcon, toggleButton)
        }

    }

    private fun initViews(view: View) {
        viewPager = view.findViewById(R.id.viewPager)
        indicator = view.findViewById(R.id.indicator)
        mapLinkTextView = view.findViewById(R.id.mapLinkTextView)
        ratingBar = view.findViewById(R.id.ratingBar)
        commentEditText = view.findViewById(R.id.commentEditText)
        submitReviewButton = view.findViewById(R.id.submitReviewButton)
        reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView)
        averageRatingTextView = view.findViewById(R.id.averageRatingTextView)

        reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupFirestore() {
        db = FirebaseFirestore.getInstance()
    }

    private fun setupClickListeners() {
        mapLinkTextView.setOnClickListener {
            mapUrl?.takeIf { it.isNotEmpty() }?.let { url ->
                openMap(url)
            } ?: showToast("Ссылка на карту недоступна")
        }

        submitReviewButton.setOnClickListener {
            submitReview()
        }

        view?.findViewById<Button>(R.id.btn_add_routes_Detinets)?.setOnClickListener {
            arguments?.getString("documentId")?.let { documentId ->
                getCurrentUserId()?.let { userId ->
                    saveToUserRoutes(userId, documentId)
                } ?: showToast("Пользователь не авторизован")
            } ?: showToast("Ошибка: ID достопримечательности не найден")
        }
    }

    private fun loadData() {
        arguments?.getString("documentId")?.let { landmarkId ->
            loadLandmarkData(landmarkId)
            loadReviews(landmarkId)
            loadAverageRating(landmarkId)
        } ?: showToast("Ошибка загрузки данных")
    }

    private fun loadLandmarkData(documentId: String) {
        db.collection("landmarks").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    updateUI(document)
                } else {
                    showToast("Достопримечательность не найдена")
                }
            }
            .addOnFailureListener { e ->
                showToast("Ошибка загрузки: ${e.message}")
            }
    }

    private fun updateUI(document: com.google.firebase.firestore.DocumentSnapshot) {
        // Name and Description
        view?.findViewById<TextView>(R.id.nameLandmark)?.text = document.getString("name") ?: ""

        // Форматируем описание маршрута с помощью Html.fromHtml()
        val routeDescription = document.getString("description") ?: ""
        val formattedText = Html.fromHtml(routeDescription, Html.FROM_HTML_MODE_COMPACT)
        view?.findViewById<TextView>(R.id.landmarkDescription)?.text = formattedText

        // Images
        val images = (document.get("img") as? Map<*, *>)?.values?.filterIsInstance<String>() ?: emptyList()
        imageUrls = images
        setupImageSlider(images)

        // Map URL
        mapUrl = (document.get("maps") as? Map<*, *>)?.get(document.getString("name")) as? String
        mapLinkTextView.text = if (!mapUrl.isNullOrEmpty()) "Открыть карту" else "Ссылка отсутствует"
    }

    private fun setupImageSlider(imageUrls: List<String>) {
        if (imageUrls.isEmpty()) {
            viewPager.visibility = View.GONE
            indicator.visibility = View.GONE
            return
        }

        viewPager.adapter = ImageSliderAdapter(imageUrls)
        indicator.text = "1/${imageUrls.size}"

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                indicator.text = "${position + 1}/${imageUrls.size}"
            }
        })
    }

    private fun submitReview() {
        val user = FirebaseAuth.getInstance().currentUser
        val landmarkId = arguments?.getString("documentId")

        if (user == null) {
            showToast("Войдите в аккаунт")
            return
        }

        if (landmarkId == null) {
            showToast("Ошибка: ID достопримечательности не найден")
            return
        }

        val rating = ratingBar.rating
        val comment = commentEditText.text.toString().trim()

        if (rating == 0f) {
            showToast("Поставьте оценку")
            return
        }

        if (comment.isEmpty()) {
            showToast("Введите текст отзыва")
            return
        }

        val review = Review(
            userId = user.uid,
            landmarkId = landmarkId,
            rating = rating,
            comment = comment,
            timestamp = Date() // Добавляем текущую дату явно
        )

        db.collection("reviews")
            .add(review)
            .addOnSuccessListener {
                updateAverageRating(landmarkId, rating)
                commentEditText.text.clear()
                ratingBar.rating = 0f
                loadReviews(landmarkId)
                showToast("Отзыв сохранен")
            }
            .addOnFailureListener {
                showToast("Ошибка сохранения")
            }
    }

    private fun loadReviews(landmarkId: String) {
        db.collection("reviews")
            .whereEqualTo("landmarkId", landmarkId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val reviews = result.documents.mapNotNull {
                    try {
                        it.toObject<Review>()
                    } catch (e: Exception) {
                        Log.e("LoadReviews", "Error parsing review: ${e.message}")
                        null
                    }
                }
                reviewsRecyclerView.adapter = ReviewsAdapter(reviews)
            }
            .addOnFailureListener { e ->
                Log.e("LoadReviews", "Error: ${e.message}", e)
                showToast("Ошибка загрузки отзывов")
            }
    }
    private fun loadAverageRating(landmarkId: String) {
        db.collection("landmarks").document(landmarkId)
            .get()
            .addOnSuccessListener { document ->
                val average = document.getDouble("averageRating") ?: 0.0
                averageRatingTextView.text = "Средний рейтинг: %.1f".format(Locale.getDefault(), average)
            }
    }

    private fun updateAverageRating(landmarkId: String, newRating: Float) {
        db.runTransaction { transaction ->
            val docRef = db.collection("landmarks").document(landmarkId)
            val snapshot = transaction.get(docRef)

            val currentTotal = snapshot.getDouble("totalRating") ?: 0.0
            val currentCount = snapshot.getLong("reviewCount") ?: 0L

            val newTotal = currentTotal + newRating
            val newCount = currentCount + 1
            val newAverage = newTotal / newCount

            transaction.update(docRef,
                "totalRating", newTotal,
                "reviewCount", newCount,
                "averageRating", newAverage
            )
        }.addOnFailureListener { e ->
            Log.e("LandmarkFragment", "Transaction failure: ${e.message}")
        }
    }

    private fun openMap(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            ContextCompat.startActivity(requireContext(), intent, null)
        } catch (e: Exception) {
            showToast("Не удалось открыть карту: ${e.message}")
        }
    }

    private fun saveToUserRoutes(userId: String, landmarkId: String) {
        val firstImageUrl = imageUrls.firstOrNull() ?: ""

        db.collection("users").document(userId)
            .collection("routes").document(landmarkId)
            .set(mapOf(
                "landmark_id" to landmarkId,
                "name" to view?.findViewById<TextView>(R.id.nameLandmark)?.text.toString(),
                "description" to view?.findViewById<TextView>(R.id.landmarkDescription)?.text.toString(),
                "maps" to mapUrl,
                "imageUrls" to firstImageUrl
            ))
            .addOnSuccessListener { showToast("Добавлено в маршруты") }
            .addOnFailureListener { e -> showToast("Ошибка: ${e.message}") }
    }

    private fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid
    private fun showToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    companion object {
        fun newInstance(documentId: String) = LandmarkFragment().apply {
            arguments = Bundle().apply { putString("documentId", documentId) }
        }
    }

    private fun toggleReviewsVisibility(recyclerView: RecyclerView, arrow: ImageView, button: TextView) {
        val transition = TransitionManager.beginDelayedTransition(recyclerView.parent as ViewGroup)

        if (isReviewsVisible) {
            recyclerView.visibility = View.VISIBLE
            button.text = "Скрыть отзывы"
            arrow.rotation = 180f
        } else {
            recyclerView.visibility = View.GONE
            button.text = "Показать отзывы"
            arrow.rotation = 0f
        }
    }

    class ReviewsAdapter(private val reviews: List<Review>) :
        RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {
        class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val userRating: RatingBar = view.findViewById(R.id.userRatingBar)
            val commentText: TextView = view.findViewById(R.id.commentTextView)
            val dateText: TextView = view.findViewById(R.id.dateTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_review, parent, false)
            return ReviewViewHolder(view)
        }

        override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
            val review = reviews[position]
            holder.userRating.rating = review.rating
            holder.commentText.text = review.comment
            review.timestamp?.let {
                holder.dateText.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(it)
            } ?: run {
                holder.dateText.text = ""
            }
        }

        override fun getItemCount() = reviews.size
    }
}
data class Review(
    val userId: String = "",
    val landmarkId: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)