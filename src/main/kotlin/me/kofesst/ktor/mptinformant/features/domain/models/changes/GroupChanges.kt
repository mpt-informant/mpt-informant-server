package me.kofesst.ktor.mptinformant.features.domain.models.changes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupChanges(
    @SerialName("group_id")
    val groupId: String = "",

    @SerialName("days")
    val days: List<GroupChangesDay> = emptyList(),
)
