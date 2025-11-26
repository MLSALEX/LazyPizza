package com.alexmls.lazypizza.core.common.time

import java.time.LocalDateTime
import java.time.ZoneId

interface Clock {
    fun now(): LocalDateTime
}

class SystemClock(
    private val zoneId: ZoneId = ZoneId.systemDefault()
) : Clock {
    override fun now(): LocalDateTime = LocalDateTime.now(zoneId)
}