package com.alexmls.lazypizza.catalog.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

class FirestoreToppingDataSource(
    private val firestore: FirebaseFirestore
) {
    fun observeToppings(): Flow<List<RemoteTopping>> = callbackFlow {
        val reg = firestore.collection("toppings")
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    close(err)
                    return@addSnapshotListener
                }
                val list = snap?.documents.orEmpty()
                    .mapNotNull { it.toObject(RemoteTopping::class.java) }
                    .filter { it.is_active }
                trySend(list).isSuccess
            }
        awaitClose { reg.remove() }
    }
}

private fun QuerySnapshot.toRemoteList(): List<RemoteProduct> {
    return documents.mapNotNull { it.toObject(RemoteProduct::class.java) }
        .filter { it.is_active }
}