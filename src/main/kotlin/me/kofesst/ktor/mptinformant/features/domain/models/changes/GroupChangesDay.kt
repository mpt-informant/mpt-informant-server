package me.kofesst.ktor.mptinformant.features.domain.models.changes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.kofesst.ktor.mptinformant.features.domain.models.DayOfWeek

@Serializable
data class GroupChangesDay(
    @SerialName("timestamp")
    val timestamp: Long,

    @Transient
    val dayOfWeek: DayOfWeek = DayOfWeek.Sunday,

    @SerialName("day_index")
    val dayIndex: Int = dayOfWeek.ordinal,

    @SerialName("day_name")
    val dayName: String = dayOfWeek.displayName,

    @SerialName("rows")
    val rows: List<GroupChangesRow>,
)
