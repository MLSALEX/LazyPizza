package com.alexmls.lazypizza.app.presentation.shell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.app.navigation.NavTab
import com.alexmls.lazypizza.app.navigation.NavigationBarState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class MainViewModel (
//    observeCartCount: ObserveCartCountUseCase
) : ViewModel() {
    private val _active = MutableStateFlow<NavTab>(NavTab.Menu)

    private fun observeCartCount(): Flow<Int> = flowOf(1)

    val navBarState: StateFlow<NavigationBarState> =
        combine(_active, observeCartCount()) { tab, count ->
            NavigationBarState(activeTab = tab, cartCount = count)
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NavigationBarState(NavTab.Menu, 0)
            )

    fun onTabSelected(tab: NavTab) {
        if (_active.value != tab) _active.value = tab
    }

}