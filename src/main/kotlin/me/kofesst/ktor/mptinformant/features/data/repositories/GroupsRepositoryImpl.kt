package me.kofesst.ktor.mptinformant.features.data.repositories

import me.kofesst.ktor.mptinformant.features.domain.models.Group
import me.kofesst.ktor.mptinformant.features.domain.repositories.DepartmentsRepository
import me.kofesst.ktor.mptinformant.features.domain.repositories.GroupsRepository

class GroupsRepositoryImpl(
    private val departmentsRepository: DepartmentsRepository,
) : GroupsRepository {
    override suspend fun getGroups(): List<Group> =
        departmentsRepository.getDepartments().map { department ->
            department.groups
        }.flatten()

    override suspend fun getGroup(idOrName: String): Group? =
        getGroups().firstOrNull { group ->
            group.id == idOrName || group.name.contains(idOrName, ignoreCase = true)
        }
}
