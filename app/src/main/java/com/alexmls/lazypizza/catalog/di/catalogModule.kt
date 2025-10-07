package com.alexmls.lazypizza.catalog.di

import com.alexmls.lazypizza.catalog.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val catalogModule = module {
    viewModelOf(::HomeViewModel)
}