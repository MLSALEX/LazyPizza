package com.alexmls.lazypizza.catalog.domain.usecase

import com.alexmls.lazypizza.catalog.domain.MissingProductException
import com.alexmls.lazypizza.catalog.domain.MissingToppingsException
import com.alexmls.lazypizza.catalog.domain.model.Category
import com.alexmls.lazypizza.catalog.domain.model.Product
import com.alexmls.lazypizza.catalog.domain.model.Topping
import com.alexmls.lazypizza.catalog.domain.repo.ProductRepository
import com.alexmls.lazypizza.catalog.domain.repo.ToppingRepository
import com.alexmls.lazypizza.core.domain.cart.AddToCartPayload
import com.alexmls.lazypizza.core.domain.cart.CartWriteApi
import com.alexmls.lazypizza.core.domain.cart.ToppingEntry
import com.google.common.truth.Truth.assertThat
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class AddProductToCartUseCaseTest {
    private lateinit var productRepo: ProductRepository
    private lateinit var toppingRepo: ToppingRepository
    private lateinit var cart: CartWriteApi
    private lateinit var sut: AddProductToCartUseCase

    @Before
    fun setUp() {
        productRepo = mockk()
        toppingRepo = mockk()
        cart = mockk(relaxed = true)
        sut = AddProductToCartUseCase(productRepo, toppingRepo, cart)
    }

    @Test
    fun `happy path - builds payload and calls cart once`() = runTest {
        coEvery { productRepo.getById("p1") } returns Product(
            id = "p1",
            name = "Margherita",
            description = "Classic",
            imageUrl = "img://p1",
            priceCents = 1299,
            category = Category.Pizza
        )
        coEvery { toppingRepo.getByIds(listOf("t1", "t2")) } returns listOf(
            Topping("t1","Cheese",200,"img://t1"),
            Topping("t2","Olives",150,"img://t2")
        )

        val selections = listOf(
            ToppingSelection("t2", 1),
            ToppingSelection("t1", 2)
        )

        sut(productId = "p1", selections = selections, quantity = 3)

        val payload = slot<AddToCartPayload>()
        coVerify(exactly = 1) { cart.addToCart(capture(payload)) }
        confirmVerified(cart)

        assertThat(payload.captured.productId).isEqualTo("p1")
        assertThat(payload.captured.productName).isEqualTo("Margherita")
        assertThat(payload.captured.imageUrl).isEqualTo("img://p1")
        assertThat(payload.captured.basePriceCents).isEqualTo(1299)
        assertThat(payload.captured.quantity).isEqualTo(3)

        // toppings sorted by id: t1, t2
        assertThat(payload.captured.toppings.map { it.id }).containsExactly("t1", "t2")
        assertThat(payload.captured.toppings[0]).isEqualTo(
            ToppingEntry(id = "t1", name = "Cheese", unitPriceCents = 200, units = 2)
        )
        assertThat(payload.captured.toppings[1]).isEqualTo(
            ToppingEntry(id = "t2", name = "Olives", unitPriceCents = 150, units = 1)
        )
    }

    @Test
    fun `product not found - throws MissingProductException and does not call cart`() = runTest {
        coEvery { productRepo.getById("nope") } returns null

        val ex = assertFailsWith<MissingProductException> {
            sut(productId = "nope", selections = emptyList(), quantity = 1)
        }
        assertThat(ex.productId).isEqualTo("nope")
        coVerify { cart wasNot Called }
    }

    @Test
    fun `some toppings missing - throws MissingToppingsException with exact ids`() = runTest {
        coEvery { productRepo.getById("p1") } returns Product(
            id = "p1", name = "Napoli", description = null,
            imageUrl = "img://p1", priceCents = 1500, category = Category.Pizza
        )
        coEvery { toppingRepo.getByIds(listOf("t1", "tX")) } returns listOf(
            Topping("t1", "Cheese", 200, "img://t1")
        )

        val ex = assertFailsWith<MissingToppingsException> {
            sut(
                productId = "p1",
                selections = listOf(ToppingSelection("t1", 1), ToppingSelection("tX", 2)),
                quantity = 1
            )
        }
        assertThat(ex.missingIds).containsExactly("tX")
        coVerify { cart wasNot Called }
    }

    @Test
    fun `duplicates merged and non-positive units filtered`() = runTest {
        coEvery { productRepo.getById("p1") } returns Product(
            id = "p1", name = "M", description = null,
            imageUrl = "img://p1", priceCents = 1000, category = Category.Pizza
        )

        coEvery { toppingRepo.getByIds(listOf("t1")) } returns listOf(
            Topping("t1", "Cheese", 100, "img://t1")
        )

        val selections = listOf(
            ToppingSelection("t1", 2),
            ToppingSelection("t1", -1),
            ToppingSelection("t2", 0)
        )

        sut(productId = "p1", selections = selections, quantity = 1)

        val payload = slot<AddToCartPayload>()
        coVerify(exactly = 1) { cart.addToCart(capture(payload)) }
        assertThat(payload.captured.toppings).hasSize(1)
        val only = payload.captured.toppings.first()
        assertThat(only.id).isEqualTo("t1")
        assertThat(only.units).isEqualTo(1)

        coVerify(exactly = 1) { toppingRepo.getByIds(listOf("t1")) }
        confirmVerified(toppingRepo)
    }

    @Test
    fun `toppings order is deterministic - sorted by id`() = runTest {
        coEvery { productRepo.getById("p1") } returns Product(
            id = "p1", name = "M", description = null,
            imageUrl = "img://p1", priceCents = 1000, category = Category.Pizza
        )
        coEvery { toppingRepo.getByIds(listOf("a", "b", "c")) } returns listOf(
            Topping("b", "B", 1, "img://b"),
            Topping("c", "C", 1, "img://c"),
            Topping("a", "A", 1, "img://a")
        )

        sut(
            productId = "p1",
            selections = listOf(
                ToppingSelection("c", 1),
                ToppingSelection("a", 1),
                ToppingSelection("b", 1)
            ),
            quantity = 1
        )

        val payload = slot<AddToCartPayload>()
        coVerify { cart.addToCart(capture(payload)) }
        assertThat(payload.captured.toppings.map { it.id }).containsExactly("a", "b", "c").inOrder()
    }

    @Test
    fun `invalid quantity - throws IllegalArgumentException`() = runTest {
        assertFailsWith<IllegalArgumentException> {
            sut(productId = "p1", selections = emptyList(), quantity = 0)
        }
        coVerify { cart wasNot Called }
    }

    @Test
    fun `blank productId - throws IllegalArgumentException`() = runTest {
        assertFailsWith<IllegalArgumentException> {
            sut(productId = "  ", selections = emptyList(), quantity = 1)
        }
        coVerify { cart wasNot Called }
    }

}