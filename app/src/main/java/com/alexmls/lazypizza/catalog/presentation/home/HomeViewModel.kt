package com.alexmls.lazypizza.catalog.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexmls.lazypizza.catalog.presentation.home.demomenu.DemoMenu
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max

class HomeViewModel (
//    private val menuRepository: MenuRepository = DemoMenuRepository
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true

                _state.update {
                    it.copy(
                        isLoading = false,
                        items = DemoMenu.all
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.ClickPhone -> sendEvent(HomeEvent.Dial(action.number))
            is HomeAction.SearchChanged -> _state.update { it.copy(search = action.query) }
            is HomeAction.CategorySelected -> _state.update { it.copy(selected = action.category) }

            is HomeAction.OpenDetails ->  sendEvent(HomeEvent.NavigateToDetails(action.id))

            is HomeAction.Add -> mutateQty(action.id) { it + 1 }
            is HomeAction.Inc -> mutateQty(action.id) { it + 1 }
            is HomeAction.Dec -> mutateQty(action.id) { max(0, it - 1) }
            is HomeAction.Remove -> _state.update { s ->
                s.copy(qty = s.qty - action.id)
            }
        }
    }

    private fun mutateQty(id: String, f: (Int) -> Int) {
        _state.update { s ->
            val cur = s.qty[id] ?: 0
            val next = f(cur)
            s.copy(qty = if (next == 0) s.qty - id else s.qty + (id to next))
        }
    }
    private fun sendEvent(e: HomeEvent) = viewModelScope.launch {
        _events.send(e)
    }
}