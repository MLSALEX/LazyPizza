package com.alexmls.lazypizza.cart_checkout.domain.usecase

import com.alexmls.lazypizza.cart_checkout.domain.model.PickupScheduleConstraints
import com.alexmls.lazypizza.cart_checkout.domain.model.TimeValidationResult
import com.alexmls.lazypizza.core.common.time.Clock
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.LocalTime
import com.google.common.truth.Truth.assertThat
import java.time.LocalDate

private class FakeClock(
    private var current: LocalDateTime
) : Clock {

    override fun now(): LocalDateTime = current

    fun setNow(newNow: LocalDateTime) {
        current = newNow
    }
}

class ValidatePickupDateTimeUseCaseTest {

    private lateinit var clock: FakeClock
    private lateinit var constraints: PickupScheduleConstraints
    private lateinit var useCase: ValidatePickupDateTimeUseCase

    @Before
    fun setUp() {
        constraints = PickupScheduleConstraints(
            openTime = LocalTime.of(10, 15),
            closeTime = LocalTime.of(21, 45),
            minLeadMinutes = 15L
        )

        clock = FakeClock(
            LocalDateTime.of(2024, 1, 1, 10, 0) // 10:00 01.01.2024
        )

        useCase = ValidatePickupDateTimeUseCase(
            clock = clock,
            constraints = constraints
        )
    }
    @Test
    fun `earliestAvailableDateTime - before open and base before open - clamps to open time`() {
        // now = 09:50, minLead = 15 => base = 10:05 (< open 10:15) => earliest = 10:15
        clock.setNow(LocalDateTime.of(2024, 1, 1, 9, 50))

        val earliest = useCase.earliestAvailableDateTime()

        assertThat(earliest.toLocalTime()).isEqualTo(LocalTime.of(10, 15))
    }

    @Test
    fun `earliestAvailableDateTime - before open but base after open - stays base`() {
        // now = 10:05, minLead = 15 => base = 10:20 (> open 10:15) => earliest = 10:20
        clock.setNow(LocalDateTime.of(2024, 1, 1, 10, 5))

        val earliest = useCase.earliestAvailableDateTime()

        assertThat(earliest.toLocalTime()).isEqualTo(LocalTime.of(10, 20))
    }

    @Test
    fun `earliestAvailableDateTime - after close moves to tomorrow at open`() {
        // now = 22:00, minLead = 15 => base 22:15 (> close 21:45) => earliest = tomorrow 10:15
        clock.setNow(LocalDateTime.of(2024, 1, 1, 22, 0))

        val earliest = useCase.earliestAvailableDateTime()

        assertThat(earliest.toLocalDate()).isEqualTo(LocalDate.of(2024, 1, 2))
        assertThat(earliest.toLocalTime()).isEqualTo(LocalTime.of(10, 15))
    }


    @Test
    fun `validate - null date or time returns null`() {
        val result1 = useCase.validate(null, LocalTime.of(12, 0))
        val result2 = useCase.validate(LocalDate.of(2024, 1, 1), null)

        assertThat(result1).isNull()
        assertThat(result2).isNull()
    }

    @Test
    fun `validate - time before working hours returns OutsideWorkingHours`() {
        // now = 10:00
        clock.setNow(LocalDateTime.of(2024, 1, 1, 10, 0))

        val date = LocalDate.of(2024, 1, 1)
        val time = LocalTime.of(9, 0) // до 10:15

        val result = useCase.validate(date, time)

        assertThat(result).isEqualTo(TimeValidationResult.OutsideWorkingHours)
    }

    @Test
    fun `validate - time after working hours returns OutsideWorkingHours`() {
        clock.setNow(LocalDateTime.of(2024, 1, 1, 20, 0))

        val date = LocalDate.of(2024, 1, 1)
        val time = LocalTime.of(22, 0) // после 21:45

        val result = useCase.validate(date, time)

        assertThat(result).isEqualTo(TimeValidationResult.OutsideWorkingHours)
    }

    @Test
    fun `validate - selected before earliestAllowed returns TooEarlyFromNow`() {
        // now = 10:05, minLead = 15 => earliestAllowed = 10:20
        clock.setNow(LocalDateTime.of(2024, 1, 1, 10, 5))

        val date = LocalDate.of(2024, 1, 1)
        val time = LocalTime.of(10, 15)

        val result = useCase.validate(date, time)

        assertThat(result).isEqualTo(TimeValidationResult.TooEarlyFromNow)
    }

    @Test
    fun `validate - selected equals earliestAllowed returns Ok`() {
        // now = 10:00, minLead = 15 => earliestAllowed = 10:15 (и это равно openTime)
        clock.setNow(LocalDateTime.of(2024, 1, 1, 10, 0))

        val date = LocalDate.of(2024, 1, 1)
        val time = LocalTime.of(10, 15)

        val result = useCase.validate(date, time)

        assertThat(result).isEqualTo(TimeValidationResult.Ok)
    }

    @Test
    fun `validate - selected after earliestAllowed and within working hours returns Ok`() {
        // now = 10:05, minLead = 15 => base 10:20 => earliestAllowed = 10:20
        clock.setNow(LocalDateTime.of(2024, 1, 1, 10, 5))

        val date = LocalDate.of(2024, 1, 1)
        val time = LocalTime.of(10, 25)

        val result = useCase.validate(date, time)

        assertThat(result).isEqualTo(TimeValidationResult.Ok)
    }

    @Test
    fun `validate - selected tomorrow before open returns OutsideWorkingHours`() {
        // now = 22:00, earliestAllowed = tomorrow at 10:15
        clock.setNow(LocalDateTime.of(2024, 1, 1, 22, 0))

        val tomorrow = LocalDate.of(2024, 1, 2)
        val time = LocalTime.of(10, 0)

        val result = useCase.validate(tomorrow, time)

        assertThat(result).isEqualTo(TimeValidationResult.OutsideWorkingHours)
    }

    @Test
    fun `validate - selected tomorrow after earliestAllowed returns Ok`() {
        // now = 22:00, earliestAllowed = tomorrow 10:15
        clock.setNow(LocalDateTime.of(2024, 1, 1, 22, 0))

        val tomorrow = LocalDate.of(2024, 1, 2)
        val time = LocalTime.of(10, 30)

        val result = useCase.validate(tomorrow, time)

        assertThat(result).isEqualTo(TimeValidationResult.Ok)
    }


    @Test
    fun `isDateSelectable - yesterday returns false`() {
        clock.setNow(LocalDateTime.of(2024, 1, 10, 12, 0))

        val yesterday = LocalDate.of(2024, 1, 9)

        assertThat(useCase.isDateSelectable(yesterday)).isFalse()
    }

    @Test
    fun `isDateSelectable - today returns true`() {
        clock.setNow(LocalDateTime.of(2024, 1, 10, 12, 0))

        val today = LocalDate.of(2024, 1, 10)

        assertThat(useCase.isDateSelectable(today)).isTrue()
    }

    @Test
    fun `isDateSelectable - tomorrow returns true`() {
        clock.setNow(LocalDateTime.of(2024, 1, 10, 12, 0))

        val tomorrow = LocalDate.of(2024, 1, 11)

        assertThat(useCase.isDateSelectable(tomorrow)).isTrue()
    }


}