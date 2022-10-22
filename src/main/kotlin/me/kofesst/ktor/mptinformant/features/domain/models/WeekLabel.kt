package me.kofesst.ktor.mptinformant.features.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class WeekLabel(
    @Transient
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
