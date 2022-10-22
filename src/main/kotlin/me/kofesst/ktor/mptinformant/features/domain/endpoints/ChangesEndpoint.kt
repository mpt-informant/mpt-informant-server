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

@Suppress("unused") // Nested location class with outer location class parameter
@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/changes")
class ChangesEndpoint {
    @Location("/byId/{groupId}")
    data class ById(val parent: ChangesEndpoint, val groupId: String)

    @Location("/byName/{groupName}")
    data class ByName(val parent: ChangesEndpoint, val groupName: String)
}

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.changesEndpoints() {
    val changesRepository by inject<ChangesRepository>()
    get<ChangesEndpoint.ById> { endpoint ->
        val changes = changesRepository.getChangesByGroupId(endpoint.groupId)
        respondSchedule(changes)
    }
    get<ChangesEndpoint.ByName> { endpoint ->
        val changes = changesRepository.getChangesByGroupName(endpoint.groupName)
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
