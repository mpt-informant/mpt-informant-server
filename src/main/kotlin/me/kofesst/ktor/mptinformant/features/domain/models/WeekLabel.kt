package me.kofesst.ktor.mptinformant.features.domain.models

enum class WeekLabel(
    val displayName: String,
) {
    Numerator("Числитель"),
    Denominator("Знаменатель"),
    None("None");

    companion object {
        fun byDisplayName(displayName: String) = values().firstOrNull { value ->
            value.displayName.lowercase() == displayName.lowercase()
        } ?: None
    }
}
