package me.kofesst.ktor.mptinformant.features.domain.endpoints

import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.kofesst.ktor.mptinformant.features.domain.models.WeekLabel
import me.kofesst.ktor.mptinformant.features.domain.repositories.ScheduleRepository
import org.koin.ktor.ext.inject

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/week-label")
class WeekLabelEndpoint

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.weekLabelEndpoints() {
    val scheduleRepository by inject<ScheduleRepository>()
    get<WeekLabelEndpoint> {
        val weekLabel = scheduleRepository.getWeekLabel() ?: WeekLabel.None
        call.respond(
            mapOf(
                "name" to weekLabel.name,
                "display_name" to weekLabel.displayName
            )
        )
    }
}
