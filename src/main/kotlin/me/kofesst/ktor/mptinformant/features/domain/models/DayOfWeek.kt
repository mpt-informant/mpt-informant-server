package me.kofesst.ktor.mptinformant.features.domain.models

enum class DayOfWeek(val displayName: String) {
    Sunday("Воскресенье"),
    Monday("Понедельник"),
    Tuesday("Вторник"),
    Wednesday("Среда"),
    Thursday("Четверг"),
    Friday("Пятница"),
    Saturday("Суббота");

    companion object {
        fun byDisplayName(displayName: String) = values().firstOrNull { value ->
            value.displayName.lowercase() == displayName.lowercase()
        } ?: Sunday
    }
}
