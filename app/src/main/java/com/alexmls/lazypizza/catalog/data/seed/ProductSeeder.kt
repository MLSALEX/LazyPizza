package com.alexmls.lazypizza.catalog.data.seed

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductSeeder(private val firestore: FirebaseFirestore) {

    suspend fun seedProductsOnce(products: List<SeedProduct>) {
        val batch = firestore.batch()
        val col = firestore.collection("products")
        products.forEach { p ->
            val doc = col.document(p.id)
            val data = mapOf(
                "id" to p.id,
                "name" to p.name,
                "description" to p.description,
                "price_cents" to p.priceCents,
                "category" to p.category,
                "image_path" to p.imagePath,
                "is_active" to p.isActive
            )
            batch.set(doc, data)
        }
        batch.commit().await()
    }

    suspend fun seedToppingsOnce(toppings: List<SeedTopping>) {
        val batch = firestore.batch()
        val col = firestore.collection("toppings")
        toppings.forEach { t ->
            val doc = col.document(t.id)
            val data = mapOf(
                "id" to t.id,
                "name" to t.name,
                "price_cents" to t.priceCents,
                "image_path" to t.imagePath,
                "is_active" to t.isActive
            )
            batch.set(doc, data)
        }
        batch.commit().await()
    }
}