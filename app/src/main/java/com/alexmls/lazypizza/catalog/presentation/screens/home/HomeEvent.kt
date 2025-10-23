package com.alexmls.lazypizza.catalog.presentation.screens.home

sealed interface HomeEvent {
    data class NavigateToDetails(val productId: String) : HomeEvent
    data class Dial(val number: String) : HomeEvent
    data object HapticTick : HomeEvent
}