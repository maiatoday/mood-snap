package net.maiatoday.moodsnap.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.Date

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun timestampToDate() {
        val timestamp = 1234567890L
        val date = converters.fromTimestamp(timestamp)
        assertEquals(timestamp, date?.time)
    }

    @Test
    fun dateToTimestamp() {
        val date = Date(1234567890L)
        val timestamp = converters.dateToTimestamp(date)
        assertEquals(date.time, timestamp)
    }

    @Test
    fun nullTimestampToDate() {
        val date = converters.fromTimestamp(null)
        assertNull(date)
    }

    @Test
    fun nullDateToTimestamp() {
        val timestamp = converters.dateToTimestamp(null)
        assertNull(timestamp)
    }
}
