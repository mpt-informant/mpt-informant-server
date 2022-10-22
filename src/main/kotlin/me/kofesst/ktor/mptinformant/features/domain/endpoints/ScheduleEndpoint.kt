package me.kofesst.ktor.mptinformant.features.domain.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupSchedule
import me.kofesst.ktor.mptinformant.features.domain.repositories.ScheduleRepository
import org.koin.ktor.ext.inject

@Suppress("unused") // Nested location class with outer location class parameter
@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/schedule")
class ScheduleEndpoint {
    @Location("/byId/{groupId}")
    data class ById(val parent: ScheduleEndpoint, val groupId: String)

    @Location("/byName/{groupName}")
    data class ByName(val parent: ScheduleEndpoint, val groupName: String)
}

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.scheduleEndpoints() {
    val scheduleRepository by inject<ScheduleRepository>()
    get<ScheduleEndpoint.ById> { endpoint ->
        val schedule = scheduleRepository.getScheduleByGroupId(endpoint.groupId)
        respondSchedule(schedule)
    }
    get<ScheduleEndpoint.ByName> { endpoint ->
        val schedule = scheduleRepository.getScheduleByGroupName(endpoint.groupName)
        respondSchedule(schedule)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.respondSchedule(schedule: GroupSchedule?) {
    if (schedule == null) {
        call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Schedule not found"
        )
        return
    }
    call.respond(schedule)
}
