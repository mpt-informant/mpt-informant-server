package me.kofesst.ktor.mptinformant.features.domain.models.schedule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupSchedule constructor(
    @SerialName("group_id")
    val groupId: String,

    @SerialName("days")
    val days: List<GroupScheduleDay>,
)
