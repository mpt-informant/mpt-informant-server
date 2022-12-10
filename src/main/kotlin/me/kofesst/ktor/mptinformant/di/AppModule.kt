package me.kofesst.ktor.mptinformant.di

import me.kofesst.ktor.mptinformant.config.AppConfig
import me.kofesst.ktor.mptinformant.features.data.repositories.ChangesRepositoryImpl
import me.kofesst.ktor.mptinformant.features.data.repositories.DepartmentsRepositoryImpl
import me.kofesst.ktor.mptinformant.features.data.repositories.GroupsRepositoryImpl
import me.kofesst.ktor.mptinformant.features.data.repositories.ScheduleRepositoryImpl
import me.kofesst.ktor.mptinformant.features.domain.repositories.ChangesRepository
import me.kofesst.ktor.mptinformant.features.domain.repositories.DepartmentsRepository
import me.kofesst.ktor.mptinformant.features.domain.repositories.GroupsRepository
import me.kofesst.ktor.mptinformant.features.domain.repositories.ScheduleRepository
import org.koin.dsl.module
import org.slf4j.LoggerFactory

val appModule = module {
    single {
        LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
    }
    single {
        AppConfig()
    }
    single<DepartmentsRepository> {
        DepartmentsRepositoryImpl()
    }
    single<GroupsRepository> {
        val departmentsRepository by inject<DepartmentsRepository>()
        GroupsRepositoryImpl(departmentsRepository)
    }
    single<ChangesRepository> {
        val groupsRepository by inject<GroupsRepository>()
        ChangesRepositoryImpl(groupsRepository)
    }
    single<ScheduleRepository> {
        val groupsRepository by inject<GroupsRepository>()
        val changesRepository by inject<ChangesRepository>()
        ScheduleRepositoryImpl(groupsRepository, changesRepository)
    }
}
