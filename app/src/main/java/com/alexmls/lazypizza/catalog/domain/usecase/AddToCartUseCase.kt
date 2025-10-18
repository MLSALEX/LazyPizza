package com.alexmls.lazypizza.catalog.domain.usecase

import com.alexmls.lazypizza.catalog.domain.model.CartItem
import com.alexmls.lazypizza.catalog.domain.repo.CartRepository

class AddToCartUseCase(private val repo: CartRepository) {
    suspend operator fun invoke(item: CartItem) = repo.add(item)
}