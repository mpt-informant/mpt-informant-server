package me.kofesst.ktor.mptinformant.features.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Department(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("groups")
    val groups: List<Group>,
)
