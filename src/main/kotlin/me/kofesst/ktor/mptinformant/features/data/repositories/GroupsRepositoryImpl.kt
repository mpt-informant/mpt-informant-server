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

    override suspend fun getGroupById(id: String): Group? =
        getGroups().firstOrNull { group ->
            group.id == id
        }

    override suspend fun getGroupByName(name: String): Group? =
        getGroups().firstOrNull { group ->
            with(group.name.lowercase().split(", ")) {
                when (size) {
                    0 -> false
                    1 -> group.name.lowercase() == name.lowercase()
                    else -> any { it == name.lowercase() }
                }
            }
        }
}
