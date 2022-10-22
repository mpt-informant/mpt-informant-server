package me.kofesst.ktor.mptinformant.features.domain.repositories

import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChanges

interface ChangesRepository {
    suspend fun getChangesByGroupId(groupId: String): GroupChanges?
    suspend fun getChangesByGroupName(groupName: String): GroupChanges?
}
