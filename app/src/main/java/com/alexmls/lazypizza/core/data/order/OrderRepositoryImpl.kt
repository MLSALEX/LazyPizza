package com.alexmls.lazypizza.core.data.order

import com.alexmls.lazypizza.core.domain.order.Order
import com.alexmls.lazypizza.core.domain.order.OrderRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class OrderRepositoryImpl(
    private val firestore: FirebaseFirestore
) : OrderRepository {

    private val ordersCollection get() = firestore.collection("orders")

    override suspend fun placeOrder(order: Order): Order {
        val dto = order.toRemote()

        val docRef = ordersCollection.add(dto).await()

        return order.copy(id = docRef.id)
    }

    override fun observeOrdersForUser(userId: String): Flow<List<Order>> =
        callbackFlow {
            val registration = ordersCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAtMillis", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    val list = snapshot?.documents.orEmpty().map { doc ->
                        val dto = doc.toObject(RemoteOrderDto::class.java)!!
                        dto.toDomain(doc.id)
                    }
                    trySend(list)
                }

            awaitClose { registration.remove() }
        }
}