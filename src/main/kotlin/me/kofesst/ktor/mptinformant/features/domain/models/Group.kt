package me.kofesst.ktor.mptinformant.features.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("department_id")
    val departmentId: String,
)
