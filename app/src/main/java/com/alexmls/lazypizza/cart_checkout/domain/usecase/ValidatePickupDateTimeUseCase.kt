package com.alexmls.lazypizza.cart_checkout.domain.usecase


import com.alexmls.lazypizza.cart_checkout.domain.model.PickupScheduleConstraints
import com.alexmls.lazypizza.cart_checkout.domain.model.TimeValidationResult
import com.alexmls.lazypizza.core.common.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ValidatePickupDateTimeUseCase(
    private val clock: Clock,
    private val constraints: PickupScheduleConstraints
) {
    fun earliestAvailableDateTime(
        now: LocalDateTime = clock.now()
    ): LocalDateTime {
        val normalizedNow = now
            .withSecond(0)
            .withNano(0)

        return computeEarliestAllowedDateTime(normalizedNow)
    }

    fun validate(
        date: LocalDate?,
        time: LocalTime?
    ): TimeValidationResult? {
        if (date == null || time == null) return null

        val selectedDateTime = LocalDateTime.of(date, time)
            .withSecond(0)
            .withNano(0)

        val now = clock.now()
            .withSecond(0)
            .withNano(0)

        // working hours
        if (time.isBefore(constraints.openTime) || time.isAfter(constraints.closeTime)) {
            return TimeValidationResult.OutsideWorkingHours
        }

        val earliestAllowed = computeEarliestAllowedDateTime(now)

        // in the past
        if (selectedDateTime.isBefore(earliestAllowed)) {
            return TimeValidationResult.TooEarlyFromNow
        }
        return TimeValidationResult.Ok
    }

    fun isDateSelectable(date: LocalDate): Boolean {
        val today = clock.now().toLocalDate()
        return !date.isBefore(today)
    }

    private fun computeEarliestAllowedDateTime(
        now: LocalDateTime
    ): LocalDateTime {
        val base = now.plusMinutes(constraints.minLeadMinutes)
        val date = base.toLocalDate()
        val time = base.toLocalTime()

        val openToday = LocalDateTime.of(date, constraints.openTime)

        return when {
            time.isBefore(constraints.openTime) -> openToday
            time.isAfter(constraints.closeTime) -> {
                val tomorrow = date.plusDays(1)
                LocalDateTime.of(tomorrow, constraints.openTime)
            }
            else -> base
        }
    }
}