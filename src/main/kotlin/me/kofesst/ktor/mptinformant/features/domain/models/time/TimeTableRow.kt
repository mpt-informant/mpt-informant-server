package me.kofesst.ktor.mptinformant.features.domain.models.time

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class TimeTableRow(
    @SerialName("lesson_number")
    val lessonNumber: Int,

    @SerialName("start_time")
    @Serializable(with = LocalTimeSerializer::class)
    val startTime: LocalTime,

    @SerialName("end_time")
    @Serializable(with = LocalTimeSerializer::class)
    val endTime: LocalTime,
) {
    companion object {
        val invalid = TimeTableRow(
            lessonNumber = 0,
            startTime = LocalTime.of(0, 0),
            endTime = LocalTime.of(0, 0)
        )
    }
}
