package me.kofesst.ktor.mptinformant.features.domain.repositories

import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChanges

interface ChangesRepository {
    suspend fun getGroupChanges(groupIdOrName: String): GroupChanges?
}
