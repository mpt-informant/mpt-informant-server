package me.kofesst.ktor.mptinformant.config

import io.ktor.server.application.*
import org.koin.ktor.ext.inject

class AppConfig {
    lateinit var serverConfig: ServerConfig
}

fun Application.setupConfig() {
    val appConfig by inject<AppConfig>()
    val serverObject = environment.config.config("ktor.server")
    val debug = serverObject.property("debug").getString().toBoolean()
    appConfig.serverConfig = ServerConfig(debug)
}

data class ServerConfig(
    val debug: Boolean,
)
