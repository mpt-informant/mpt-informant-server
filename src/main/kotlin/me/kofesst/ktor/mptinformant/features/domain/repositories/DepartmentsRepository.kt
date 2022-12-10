package me.kofesst.ktor.mptinformant.features.domain.repositories

import me.kofesst.ktor.mptinformant.features.domain.models.Department

interface DepartmentsRepository {
    suspend fun getDepartments(): List<Department>
    suspend fun getDepartment(idOrName: String): Department?
}
