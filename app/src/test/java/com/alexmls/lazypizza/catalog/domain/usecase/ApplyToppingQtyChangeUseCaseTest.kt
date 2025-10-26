package com.alexmls.lazypizza.catalog.domain.usecase

import com.alexmls.lazypizza.catalog.domain.model.Topping
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class ApplyToppingQtyChangeUseCaseTest {
    private val useCase = ApplyToppingQtyChangeUseCase()

    private val toppings = listOf(
        Topping(id = "a", name = "A", priceCents = 200, imageUrl = "", maxUnits = 3),
        Topping(id = "b", name = "B", priceCents = 300, imageUrl = "", maxUnits = 2),
    )

    @Test
    fun `add first time sets to 1`() {
        val next = useCase.invoke(
            currentQty = emptyMap(),
            id = "a",
            transform = { 1 },
            toppings = toppings
        )
        assertThat(next).containsExactlyEntriesIn(mapOf("a" to 1))
    }

    @Test
    fun `increment respects maxUnits clamp`() {
        val next = useCase.invoke(
            currentQty = mapOf("b" to 2),
            id = "b",
            transform = { it + 10 }, // try to exceed
            toppings = toppings
        )
        assertThat(next["b"]).isEqualTo(2) // clamped to maxUnits=2
    }

    @Test
    fun `decrement to zero removes entry`() {
        val next = useCase.invoke(
            currentQty = mapOf("a" to 1),
            id = "a",
            transform = { it - 1 },
            toppings = toppings
        )
        assertThat(next).doesNotContainKey("a")
    }

    @Test
    fun `negative never stored`() {
        val next = useCase.invoke(
            currentQty = mapOf("a" to 0),
            id = "a",
            transform = { it - 5 },
            toppings = toppings
        )
        assertThat(next).doesNotContainKey("a")
    }

    @Test
    fun `unknown topping id has no clamp (treated as unlimited)`() {
        val next = useCase.invoke(
            currentQty = emptyMap(),
            id = "unknown",
            transform = { 5 },
            toppings = toppings
        )
        assertThat(next["unknown"]).isEqualTo(5)
    }
}