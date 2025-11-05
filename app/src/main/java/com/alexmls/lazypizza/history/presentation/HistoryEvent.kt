package com.alexmls.lazypizza.history.presentation

sealed interface HistoryEvent {
    data object NavigateToAuth : HistoryEvent
}