package com.alexmls.lazypizza.catalog.presentation.screens.home

import com.alexmls.lazypizza.catalog.presentation.model.CategoryUi

sealed interface HomeAction {

    data class ClickPhone(val number: String) : HomeAction
    data class SearchChanged(val query: String) : HomeAction
    data class CategorySelected(val category: CategoryUi?) : HomeAction // null = All

    // Product actions
    data class OpenDetails(val id: String) : HomeAction
    data class Add(val id: String) : HomeAction
    data class Inc(val id: String) : HomeAction
    data class Dec(val id: String) : HomeAction
    data class Remove(val id: String) : HomeAction
}