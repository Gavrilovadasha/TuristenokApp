package daa.gv.turistenokapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyRoutesFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoutesAdapter
    private val routesList = mutableListOf<Map<String, Any>>()
    private lateinit var emptyTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_routes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyTextView = view.findViewById(R.id.emptyTextView)
        db = FirebaseFirestore.getInstance()

        recyclerView = view.findViewById(R.id.routesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Исправленный конструктор адаптера
        adapter = RoutesAdapter(
            routesList,
            { routeId -> deleteRoute(routeId) }, // Удаление
            { landmarkId -> openLandmarkFragment(landmarkId) } // Открытие достопримечательности
        )
        recyclerView.adapter = adapter

        loadUserRoutes()
    }

    private fun updateViews() {
        recyclerView.visibility = if (routesList.isEmpty()) View.GONE else View.VISIBLE
        emptyTextView.visibility = if (routesList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun loadUserRoutes() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("users").document(userId).collection("routes")
            .get()
            .addOnSuccessListener { documents ->
                routesList.clear()
                for (document in documents) {
                    val data = document.data
                    routesList.add(mapOf(
                        "id" to document.id,
                        "name" to (data["name"] ?: ""),
                        "imageUrls" to (data["imageUrls"] ?: ""),
                        "landmark_id" to (data["landmark_id"] ?: "")
                    ))
                }
                adapter.notifyDataSetChanged()
                updateViews()
            }
    }

    private fun deleteRoute(routeId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("users").document(userId).collection("routes")
            .document(routeId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Маршрут удален", Toast.LENGTH_SHORT).show()
                loadUserRoutes()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Ошибка удаления: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openLandmarkFragment(landmarkId: String) {
        val fragment = LandmarkFragment.newInstance(landmarkId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, fragment) // Убедитесь, что ID контейнера совпадает
            .addToBackStack(null)
            .commit()
    }

    // Исправленный адаптер
    class RoutesAdapter(
        private val routesList: List<Map<String, Any>>,
        private val onDeleteClickListener: (String) -> Unit,
        private val onImageClickListener: (String) -> Unit
    ) : RecyclerView.Adapter<RoutesAdapter.RoutesViewHolder>() {

        inner class RoutesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.routeNameTextView)
            val routeImageButton: ImageButton = itemView.findViewById(R.id.routeImg)
            val deleteButton: Button = itemView.findViewById(R.id.btn_delete)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_route, parent, false)
            return RoutesViewHolder(view)
        }

        override fun onBindViewHolder(holder: RoutesViewHolder, position: Int) {
            val route = routesList[position]

            // Обработчик клика по изображению
            holder.routeImageButton.setOnClickListener {
                route["landmark_id"]?.toString()?.let { id ->
                    onImageClickListener(id)
                }
            }

            // Заполнение данных
            holder.nameTextView.text = route["name"]?.toString() ?: "Нет имени"

            Glide.with(holder.itemView.context)
                .load(route["imageUrls"]?.toString()?.replace("&amp;", "&"))
                .transform(RoundedCorners(100))
                .error(R.drawable.placeholder_image)
                .into(holder.routeImageButton)

            // Обработчик удаления
            holder.deleteButton.setOnClickListener {
                route["id"]?.toString()?.let { id ->
                    onDeleteClickListener(id)
                }
            }
        }

        override fun getItemCount() = routesList.size
    }
}