package me.kofesst.ktor.mptinformant

import me.kofesst.ktor.mptinformant.features.data.utils.mergeWithChanges
import me.kofesst.ktor.mptinformant.features.domain.models.DayOfWeek
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChanges
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChangesDay
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChangesRow
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupSchedule
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupScheduleDay
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupScheduleRow
import org.junit.jupiter.api.Test

class ScheduleMergingTests {
    companion object {
        private val testSchedule = GroupSchedule(
            groupId = "test",
            days = listOf(
                GroupScheduleDay(
                    dayOfWeek = DayOfWeek.Monday,
                    branch = "test",
                    rows = listOf(
                        GroupScheduleRow.Single(
                            lessonNumber = 1,
                            lesson = "test lesson 1",
                            teacher = "test teacher 1"
                        ),
                        GroupScheduleRow.Single(
                            lessonNumber = 2,
                            lesson = "test lesson 2",
                            teacher = "test teacher 2"
                        ),
                        GroupScheduleRow.Single(
                            lessonNumber = 3,
                            lesson = "test lesson 3",
                            teacher = "test teacher 3"
                        ),
                    )
                )
            )
        )
        private val testChanges = GroupChanges(
            groupId = "test",
            days = listOf(
                GroupChangesDay(
                    timestamp = 0,
                    dayOfWeek = DayOfWeek.Monday,
                    rows = listOf(
                        GroupChangesRow.Moved(
                            lesson = "test lesson 3",
                            teacher = "test teacher 3",
                            movedFrom = 3,
                            movedTo = 5,
                            insertTimestamp = 0
                        ),
                        GroupChangesRow.Canceled(
                            lesson = "test lesson 2",
                            teacher = "test teacher 2",
                            lessonNumber = 2,
                            insertTimestamp = 0,
                            cause = GroupChangesRow.Canceled.CancelCause.DueProgram
                        ),
                        GroupChangesRow.Additional(
                            lesson = "test lesson 100",
                            teacher = "test teacher 100",
                            lessonNumber = 4,
                            insertTimestamp = 0,
                        ),
                        GroupChangesRow.Replace(
                            replacedLesson = "test lesson 1",
                            replacedTeacher = "test teacher 1",
                            replacementLesson = "REPLACEMENT LESSON 1",
                            replacementTeacher = "REPLACEMENT TEACHER 1",
                            lessonNumber = 1,
                            insertTimestamp = 0,
                        ),
                    )
                )
            )
        )
    }

    @Test
    fun `Group schedule merging works as expected`() {
        val mergedSchedule = testSchedule.mergeWithChanges(testChanges)
        val actualRows = mergedSchedule.days.first().rows
        assert(actualRows.size == 3) { "Некорректный размер строк расписания" }
        assert(actualRows[0].lessonNumber == 1) { "Некорректный номер пары у первой строки расписания" }
        assert(actualRows[1].lessonNumber == 4) { "Некорректный номер пары у второй строки расписания" }
        assert(actualRows[2].lessonNumber == 5) { "Некорректный номер пары у третьей строки расписания" }
    }
}
