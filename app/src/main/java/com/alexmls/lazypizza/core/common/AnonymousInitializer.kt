package com.alexmls.lazypizza.core.common


import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AnonymousAuthInitializer(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun ensureSignedIn() {
        if (firebaseAuth.currentUser == null) {
            firebaseAuth.signInAnonymously().await()
        }
    }
}