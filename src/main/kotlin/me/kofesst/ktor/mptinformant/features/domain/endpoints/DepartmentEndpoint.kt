package me.kofesst.ktor.mptinformant.features.domain.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import me.kofesst.ktor.mptinformant.features.domain.models.Department
import me.kofesst.ktor.mptinformant.features.domain.repositories.DepartmentsRepository
import org.koin.ktor.ext.inject

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/departments")
class DepartmentEndpoint

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.departmentEndpoints() {
    val departmentsRepository by inject<DepartmentsRepository>()
    get<DepartmentEndpoint> {
        when (val departmentParam = call.parameters["department"]) {
            null -> call.respond(departmentsRepository.getDepartments())
            else -> {
                val department = departmentsRepository.getDepartment(departmentParam)
                respondDepartment(department)
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.respondDepartment(department: Department?) {
    if (department == null) {
        call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Department not found"
        )
        return
    }
    call.respond(department)
}
