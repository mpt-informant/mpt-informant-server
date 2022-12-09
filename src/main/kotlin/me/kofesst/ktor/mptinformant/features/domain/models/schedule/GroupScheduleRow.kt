package me.kofesst.ktor.mptinformant.features.domain.models.schedule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.kofesst.ktor.mptinformant.features.domain.models.time.TimeTable
import me.kofesst.ktor.mptinformant.features.domain.models.time.TimeTableRow

@Serializable
sealed class GroupScheduleRow {
    abstract val lessonNumber: Int

    @Serializable
    @SerialName("single")
    data class Single(
        @SerialName("lesson_number")
        override val lessonNumber: Int,

        @SerialName("lesson")
        val lesson: String,

        @SerialName("teacher")
        val teacher: String,
    ) : GroupScheduleRow() {
        @SerialName("time_table")
        val timeTableRow: TimeTableRow = TimeTable.default.of(lessonNumber)
    }

    @Serializable
    @SerialName("divided")
    data class Divided(
        @SerialName("lesson_number")
        override val lessonNumber: Int,

        @SerialName("numerator")
        val numerator: Label,

        @SerialName("denominator")
        val denominator: Label,
    ) : GroupScheduleRow() {
        @SerialName("time_table")
        val timeTableRow: TimeTableRow = TimeTable.default.of(lessonNumber)

        @Serializable
        @SerialName("label")
        data class Label(
            @SerialName("lesson")
            val lesson: String,

            @SerialName("teacher")
            val teacher: String,
        ) : GroupScheduleRow() {
            @Transient
            override val lessonNumber: Int = -1
        }
    }
}
