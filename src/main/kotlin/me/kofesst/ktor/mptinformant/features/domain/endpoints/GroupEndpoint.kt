package me.kofesst.ktor.mptinformant.features.domain.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import me.kofesst.ktor.mptinformant.features.domain.models.Group
import me.kofesst.ktor.mptinformant.features.domain.repositories.GroupsRepository
import org.koin.ktor.ext.inject

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/groups")
class GroupEndpoint

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.groupEndpoints() {
    val groupsRepository by inject<GroupsRepository>()
    get<GroupEndpoint> {
        when (val groupParam = call.parameters["group"]) {
            null -> call.respond(groupsRepository.getGroups())
            else -> {
                val department = groupsRepository.getGroup(groupParam)
                respondGroup(department)
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.respondGroup(group: Group?) {
    if (group == null) {
        call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Group not found"
        )
        return
    }
    call.respond(group)
}
