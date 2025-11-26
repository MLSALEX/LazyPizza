package com.alexmls.lazypizza.core.di

import com.alexmls.lazypizza.core.data.order.OrderRepositoryImpl
import com.alexmls.lazypizza.core.domain.order.OrderRepository
import com.alexmls.lazypizza.core.domain.order.usecase.ObserveUserOrdersUseCase
import com.alexmls.lazypizza.core.domain.order.usecase.PlaceOrderUseCase
import org.koin.dsl.module

val coreModule = module {
    single<OrderRepository> { OrderRepositoryImpl(firestore = get()) }
    factory { PlaceOrderUseCase(get(), get()) }
    factory { ObserveUserOrdersUseCase(get(), get()) }
}