package me.kofesst.ktor.mptinformant.features.domain.repositories

import me.kofesst.ktor.mptinformant.features.domain.models.Group

interface GroupsRepository {
    suspend fun getGroups(): List<Group>
    suspend fun getGroup(idOrName: String): Group?
}
