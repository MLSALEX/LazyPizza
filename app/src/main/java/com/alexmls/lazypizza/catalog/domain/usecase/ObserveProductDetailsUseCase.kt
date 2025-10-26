package com.alexmls.lazypizza.catalog.domain.usecase

import com.alexmls.lazypizza.catalog.domain.model.Product
import com.alexmls.lazypizza.catalog.domain.model.Topping
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import com.alexmls.lazypizza.catalog.domain.repo.ToppingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ObserveProductDetailsUseCase(
    private val productRepo: ProductRepository,
    private val toppingRepo: ToppingRepository
) {
    operator fun invoke(productId: String): Flow<Pair<Product, List<Topping>>> =
        combine(
            productRepo.observeProducts().map { list -> list.firstOrNull { it.id == productId } },
            toppingRepo.observeToppings()
        ) { p, tops -> p to tops }
            .map { (p, tops) -> requireNotNull(p) to tops }
}