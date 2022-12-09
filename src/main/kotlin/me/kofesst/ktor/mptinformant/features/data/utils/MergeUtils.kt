package me.kofesst.ktor.mptinformant.features.data.utils

import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChanges
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChangesRow
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupSchedule
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupScheduleDay
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupScheduleRow

fun GroupSchedule.mergeWithChanges(changes: GroupChanges): GroupSchedule {
    val mergedDays = this.days.map { dailySchedule ->
        val dailyChanges = changes.days.firstOrNull { it.dayIndex == dailySchedule.dayIndex }
            ?: return@map dailySchedule
        val dailyScheduleRows = dailySchedule.rows.toMutableList()
        dailyChanges.rows.forEach { changesRow ->
            when (changesRow) {
                is GroupChangesRow.Canceled -> {
                    dailyScheduleRows.removeIf { it.lessonNumber == changesRow.lessonNumber }
                }

                is GroupChangesRow.Moved -> {
                    dailyScheduleRows.removeIf { it.lessonNumber == changesRow.movedFrom }
                    val row = changesRow.toScheduleRow()
                    dailyScheduleRows.add(row)
                }

                is GroupChangesRow.Replace -> {
                    dailyScheduleRows.removeIf { it.lessonNumber == changesRow.lessonNumber }
                    val row = changesRow.toScheduleRow()
                    dailyScheduleRows.add(row)
                }

                else -> {
                    val row = changesRow.toScheduleRow()
                    dailyScheduleRows.add(row)
                }
            }
        }
        dailySchedule.copy(
            rows = dailyScheduleRows.sortedBy { it.lessonNumber }
        )
    }.toMutableList()
    mergedDays.addAll(checkForIgnoredChanges(changes, mergedDays))
    return copy(
        days = mergedDays.sortedBy { it.dayIndex }
    )
}

private fun checkForIgnoredChanges(
    changes: GroupChanges,
    mergedDays: List<GroupScheduleDay>,
) = changes.days.filter { dailyChanges ->
    !mergedDays.any { dailySchedule ->
        dailySchedule.dayIndex == dailyChanges.dayIndex
    }
}.map { ignoredDailyChanges ->
    GroupScheduleDay(
        dayOfWeek = ignoredDailyChanges.dayOfWeek,
        branch = "",
        rows = ignoredDailyChanges.rows.map { it.toScheduleRow() }
    )
}

fun GroupChangesRow.toScheduleRow(): GroupScheduleRow {
    val (lessonNumber, lesson, teacher) = when (this) {
        is GroupChangesRow.Additional -> Triple(
            first = lessonNumber,
            second = lesson,
            third = teacher
        )

        is GroupChangesRow.Canceled -> {
            throw UnsupportedOperationException()
        }

        is GroupChangesRow.Moved -> Triple(
            first = movedTo,
            second = lesson,
            third = teacher
        )

        is GroupChangesRow.Replace -> Triple(
            first = lessonNumber,
            second = replacementLesson,
            third = replacementTeacher
        )
    }
    return GroupScheduleRow.Single(
        lessonNumber = lessonNumber,
        lesson = lesson,
        teacher = teacher,
        default = false,
    )
}
