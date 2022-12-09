package me.kofesst.ktor.mptinformant.features.domain.repositories

import me.kofesst.ktor.mptinformant.features.domain.models.WeekLabel
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupSchedule

interface ScheduleRepository {
    suspend fun getWeekLabel(): WeekLabel?
    suspend fun getGroupSchedule(groupIdOrName: String): GroupSchedule?
}
