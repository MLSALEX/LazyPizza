package com.alexmls.lazypizza.authorization.di

import com.alexmls.lazypizza.authorization.data.FakeAuthRepository
import com.alexmls.lazypizza.authorization.presentation.AuthViewModel
import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    viewModelOf(::AuthViewModel)

    single<AuthRepository> { FakeAuthRepository() }
}