package com.alexmls.lazypizza.cart_checkout.domain.model

import java.time.LocalTime

data class PickupScheduleConstraints(
    val openTime: LocalTime,
    val closeTime: LocalTime,
    val minLeadMinutes: Long       // 15
)