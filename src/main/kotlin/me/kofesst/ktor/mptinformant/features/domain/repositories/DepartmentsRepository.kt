package me.kofesst.ktor.mptinformant.features.domain.repositories

import me.kofesst.ktor.mptinformant.features.domain.models.Department

interface DepartmentsRepository {
    suspend fun getDepartments(): List<Department>
    suspend fun getDepartmentById(id: String): Department?
    suspend fun getDepartmentByName(name: String): Department?
}
