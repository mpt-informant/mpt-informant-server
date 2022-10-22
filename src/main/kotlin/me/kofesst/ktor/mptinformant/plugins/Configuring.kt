package me.kofesst.ktor.mptinformant.plugins

import io.ktor.server.application.*
import me.kofesst.ktor.mptinformant.config.setupConfig

fun Application.configureAppConfig() {
    setupConfig()
}
