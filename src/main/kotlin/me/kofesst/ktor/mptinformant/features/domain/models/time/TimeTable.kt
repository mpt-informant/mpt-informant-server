package me.kofesst.ktor.mptinformant.features.domain.models.time

import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class TimeTable(
    val rows: List<TimeTableRow>,
) {
    companion object {
        val default = TimeTable(
            rows = listOf(
                TimeTableRow(
                    lessonNumber = 1,
                    startTime = LocalTime.of(8, 30),
                    endTime = LocalTime.of(10, 0)
                ),
                TimeTableRow(
                    lessonNumber = 2,
                    startTime = LocalTime.of(10, 10),
                    endTime = LocalTime.of(11, 40)
                ),
                TimeTableRow(
                    lessonNumber = 3,
                    startTime = LocalTime.of(12, 0),
                    endTime = LocalTime.of(13, 30)
                ),
                TimeTableRow(
                    lessonNumber = 4,
                    startTime = LocalTime.of(14, 0),
                    endTime = LocalTime.of(15, 30)
                ),
                TimeTableRow(
                    lessonNumber = 5,
                    startTime = LocalTime.of(15, 40),
                    endTime = LocalTime.of(17, 10)
                ),
                TimeTableRow(
                    lessonNumber = 6,
                    startTime = LocalTime.of(17, 15),
                    endTime = LocalTime.of(18, 45)
                ),
                TimeTableRow(
                    lessonNumber = 7,
                    startTime = LocalTime.of(18, 50),
                    endTime = LocalTime.of(20, 20)
                ),
            )
        )
    }

    fun of(lessonNumber: Int) = rows.firstOrNull { row ->
        row.lessonNumber == lessonNumber
    } ?: TimeTableRow.invalid
}
