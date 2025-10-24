package com.alexmls.lazypizza.history.di


import com.alexmls.lazypizza.history.presentation.HistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val historyModule = module {
    viewModelOf(::HistoryViewModel)
}