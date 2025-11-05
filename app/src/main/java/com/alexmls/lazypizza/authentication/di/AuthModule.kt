package com.alexmls.lazypizza.authentication.di

import com.alexmls.lazypizza.authentication.data.FakeAuthRepository
import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import org.koin.dsl.module


val authModule = module {
    single<AuthRepository> { FakeAuthRepository() }
}