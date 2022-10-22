package me.kofesst.ktor.mptinformant.features.data.utils

import me.kofesst.ktor.mptinformant.features.domain.models.DayOfWeek
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(
    format: String,
    defaultValue: Date = Date(0),
): Date {
    val sdf = SimpleDateFormat(format, Locale.ROOT)
    return sdf.parse(this) ?: defaultValue
}

fun Date.calendar(): Calendar {
    return Calendar.getInstance().apply {
        time = this@calendar
    }
}

fun Calendar.getDayOfWeek(): DayOfWeek {
    val dayOfWeekIndex = this.get(Calendar.DAY_OF_WEEK)
    return DayOfWeek.values()[dayOfWeekIndex - 1]
}
