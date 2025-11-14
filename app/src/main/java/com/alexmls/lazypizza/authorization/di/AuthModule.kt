package com.alexmls.lazypizza.authorization.di

import com.alexmls.lazypizza.authorization.data.FakeAuthRepository
import com.alexmls.lazypizza.authorization.data.FirebaseAuthRepository
import com.alexmls.lazypizza.authorization.presentation.AuthViewModel
import com.alexmls.lazypizza.BuildConfig
import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    viewModelOf(::AuthViewModel)

    single<AuthRepository> {
        if (BuildConfig.USE_FAKE_AUTH) {
            FakeAuthRepository()
        } else {
            FirebaseAuthRepository(
                firebaseAuth = FirebaseAuth.getInstance(),
                activityProvider = get()
            )
        }
    }
}