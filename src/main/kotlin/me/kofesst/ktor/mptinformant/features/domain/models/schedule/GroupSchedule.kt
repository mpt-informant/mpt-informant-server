package me.kofesst.ktor.mptinformant.features.domain.models.schedule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.kofesst.ktor.mptinformant.features.domain.models.WeekLabel

@Serializable
data class GroupSchedule constructor(
    @Transient
    val weekLabel: WeekLabel = WeekLabel.None,

    @SerialName("week_label")
    val weekLabelName: String = weekLabel.displayName,

    @SerialName("group_id")
    val groupId: String,

    @SerialName("days")
    val days: List<GroupScheduleDay>,
)
