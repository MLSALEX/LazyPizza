package com.alexmls.lazypizza.history.presentation

sealed interface HistoryAction {
    data object ClickSignIn : HistoryAction
}