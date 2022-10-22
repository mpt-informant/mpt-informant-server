package me.kofesst.ktor.mptinformant.features.domain.repositories

import me.kofesst.ktor.mptinformant.features.domain.models.WeekLabel
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupSchedule

interface ScheduleRepository {
    suspend fun getWeekLabel(): WeekLabel?
    suspend fun getScheduleByGroupId(groupId: String): GroupSchedule?
    suspend fun getScheduleByGroupName(groupName: String): GroupSchedule?
}
