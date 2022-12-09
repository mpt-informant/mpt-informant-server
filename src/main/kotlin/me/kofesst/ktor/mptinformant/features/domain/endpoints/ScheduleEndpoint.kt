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

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/schedule/{group}")
data class ScheduleEndpoint(val group: String)

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.scheduleEndpoints() {
    val scheduleRepository by inject<ScheduleRepository>()
    get<ScheduleEndpoint> { endpoint ->
        val schedule = scheduleRepository.getGroupSchedule(endpoint.group)
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
