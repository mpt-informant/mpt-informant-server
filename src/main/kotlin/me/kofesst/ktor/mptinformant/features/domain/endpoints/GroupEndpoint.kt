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

@Suppress("unused") // Nested location class with outer location class parameter
@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/groups")
class GroupEndpoint {
    @Location("/all")
    class All(val parent: GroupEndpoint)

    @Location("/byId/{id}")
    data class ById(val parent: GroupEndpoint, val id: String)

    @Location("/byName/{name}")
    data class ByName(val parent: GroupEndpoint, val name: String)
}

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.groupEndpoints() {
    val groupsRepository by inject<GroupsRepository>()
    get<GroupEndpoint.All> {
        call.respond(groupsRepository.getGroups())
    }
    get<GroupEndpoint.ById> { endpoint ->
        val group = groupsRepository.getGroupById(endpoint.id)
        respondGroup(group)
    }
    get<GroupEndpoint.ByName> { endpoint ->
        val group = groupsRepository.getGroupByName(endpoint.name)
        respondGroup(group)
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
