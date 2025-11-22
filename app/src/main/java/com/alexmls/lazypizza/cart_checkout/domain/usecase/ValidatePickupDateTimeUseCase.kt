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

    fun validate(
        date: LocalDate?,
        time: LocalTime?
    ): TimeValidationResult? {
        if (date == null || time == null) return null

        val selectedDateTime = LocalDateTime.of(date, time)
        val now = clock.now()
        // in the past
        if (selectedDateTime.isBefore(now)) {
            return TimeValidationResult.TooEarlyFromNow
        }

        // today must be now + minLead
        if (date.isEqual(now.toLocalDate())) {
            val minTimeToday = now.plusMinutes(constraints.minLeadMinutes)
            if (selectedDateTime.isBefore(minTimeToday)) {
                return TimeValidationResult.TooEarlyFromNow
            }
        }

        // working hours
        if (time.isBefore(constraints.openTime) || time.isAfter(constraints.closeTime)) {
            return TimeValidationResult.OutsideWorkingHours
        }

        return TimeValidationResult.Ok
    }

    fun isDateSelectable(date: LocalDate): Boolean {
        val today = clock.now().toLocalDate()
        return !date.isBefore(today)
    }
}