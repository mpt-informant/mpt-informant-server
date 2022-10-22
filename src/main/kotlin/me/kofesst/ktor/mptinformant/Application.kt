package me.kofesst.ktor.mptinformant

import io.ktor.server.application.*
import io.ktor.server.netty.*
import me.kofesst.ktor.mptinformant.di.appModule
import me.kofesst.ktor.mptinformant.plugins.configureAppConfig
import me.kofesst.ktor.mptinformant.plugins.configureInjecting
import me.kofesst.ktor.mptinformant.plugins.configureRouting
import me.kofesst.ktor.mptinformant.plugins.configureSerialization
import org.koin.core.module.Module

fun main(args: Array<String>) =
    EngineMain.main(args)

@Suppress("unused") // Used in application.conf
@JvmOverloads
fun Application.module(koinModules: List<Module> = listOf(appModule)) {
    configureInjecting(koinModules)
    configureAppConfig()
    configureSerialization()
    configureRouting()
}
