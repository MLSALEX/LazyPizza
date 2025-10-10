package com.alexmls.lazypizza.catalog.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

data class RemoteProduct(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price_cents: Int = 0,
    val category: String = "",
    val image_path: String? = null,
    val image_url: String? = null,
    val is_active: Boolean = true
)

class FirestoreProductDataSource(
    private val firestore: FirebaseFirestore
) {
    fun observeProducts(): Flow<List<RemoteProduct>> = callbackFlow {
        val registration = firestore
            .collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list: List<RemoteProduct> = snapshot?.toRemoteList() ?: emptyList()
                trySend(list).isSuccess
            }
        awaitClose { registration.remove() }
    }
}

private fun QuerySnapshot.toRemoteList(): List<RemoteProduct> {
    return documents.mapNotNull { it.toObject(RemoteProduct::class.java) }
        .filter { it.is_active }
}