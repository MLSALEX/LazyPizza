package com.alexmls.lazypizza.catalog.domain.usecase

import com.alexmls.lazypizza.catalog.domain.repo.CartRepository

class ObserveCartCountUseCase(private val repo: CartRepository) {
    operator fun invoke() = repo.observeCount()
}