package daa.gv.turistenokapp

import com.google.firebase.firestore.FirebaseFirestore

private val routesCollection = FirebaseFirestore.getInstance().collection("routes")

data class Routes(
    val id: String = "",
    val name: String = "",
    val landmarks: MutableList<String> = mutableListOf()
)
