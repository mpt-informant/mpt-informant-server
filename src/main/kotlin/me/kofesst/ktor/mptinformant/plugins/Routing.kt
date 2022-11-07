package me.kofesst.ktor.mptinformant.plugins

import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.routing.*
import me.kofesst.ktor.mptinformant.features.domain.endpoints.*

@Suppress("UnnecessaryOptInAnnotation")
@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureRouting() {
    install(Locations)
    routing {
        departmentEndpoints()
        groupEndpoints()
        scheduleEndpoints()
        changesEndpoints()
        timeTableEndpoints()
    }
}
