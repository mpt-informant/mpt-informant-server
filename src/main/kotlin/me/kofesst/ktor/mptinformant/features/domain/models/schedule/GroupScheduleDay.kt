package me.kofesst.ktor.mptinformant.features.domain.models.schedule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.kofesst.ktor.mptinformant.features.domain.models.DayOfWeek

@Serializable
data class GroupScheduleDay(
    @Transient
    val dayOfWeek: DayOfWeek = DayOfWeek.Sunday,

    @SerialName("day_index")
    val dayIndex: Int = dayOfWeek.ordinal,

    @SerialName("day_name")
    val dayName: String = dayOfWeek.displayName,

    @SerialName("branch")
    val branch: String,

    @SerialName("rows")
    val rows: List<GroupScheduleRow>,
)
