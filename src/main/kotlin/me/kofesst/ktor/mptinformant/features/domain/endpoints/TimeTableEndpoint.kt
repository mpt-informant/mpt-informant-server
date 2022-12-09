package me.kofesst.ktor.mptinformant.features.domain.endpoints

import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.kofesst.ktor.mptinformant.features.domain.models.time.TimeTable

@Suppress("unused") // Nested location class with outer location class parameter
@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/timetable")
class TimeTableEndpoint

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.timeTableEndpoints() {
    get<TimeTableEndpoint> {
        call.respond(TimeTable.default)
    }
}
