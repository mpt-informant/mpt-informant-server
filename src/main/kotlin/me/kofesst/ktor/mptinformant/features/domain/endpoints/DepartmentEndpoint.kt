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

@Suppress("unused") // Nested location class with outer location class parameter
@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/departments")
class DepartmentEndpoint {
    @Location("/all")
    class All(val parent: DepartmentEndpoint)

    @Location("/byId/{id}")
    data class ById(val parent: DepartmentEndpoint, val id: String)

    @Location("/byName/{name}")
    data class ByName(val parent: DepartmentEndpoint, val name: String)
}

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.departmentEndpoints() {
    val departmentsRepository by inject<DepartmentsRepository>()
    get<DepartmentEndpoint.All> {
        call.respond(departmentsRepository.getDepartments())
    }
    get<DepartmentEndpoint.ById> { endpoint ->
        val department = departmentsRepository.getDepartmentById(endpoint.id)
        respondDepartment(department)
    }
    get<DepartmentEndpoint.ByName> { endpoint ->
        val department = departmentsRepository.getDepartmentByName(endpoint.name)
        respondDepartment(department)
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
