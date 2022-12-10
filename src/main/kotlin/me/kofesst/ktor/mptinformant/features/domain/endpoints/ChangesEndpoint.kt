package me.kofesst.ktor.mptinformant.features.domain.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChanges
import me.kofesst.ktor.mptinformant.features.domain.repositories.ChangesRepository
import org.koin.ktor.ext.inject

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/changes/{idOrName}")
data class ChangesEndpoint(val idOrName: String)

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.changesEndpoints() {
    val changesRepository by inject<ChangesRepository>()
    get<ChangesEndpoint> { endpoint ->
        val changes = changesRepository.getGroupChanges(endpoint.idOrName)
        respondSchedule(changes)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.respondSchedule(changes: GroupChanges?) {
    if (changes == null) {
        call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Changes not found"
        )
        return
    }
    call.respond(changes)
}
