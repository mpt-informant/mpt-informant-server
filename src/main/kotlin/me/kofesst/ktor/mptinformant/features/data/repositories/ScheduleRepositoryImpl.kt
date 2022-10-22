package me.kofesst.ktor.mptinformant.features.data.repositories

import me.kofesst.ktor.mptinformant.features.data.utils.parseDocument
import me.kofesst.ktor.mptinformant.features.domain.models.DayOfWeek
import me.kofesst.ktor.mptinformant.features.domain.models.WeekLabel
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupSchedule
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupScheduleDay
import me.kofesst.ktor.mptinformant.features.domain.models.schedule.GroupScheduleRow
import me.kofesst.ktor.mptinformant.features.domain.repositories.GroupsRepository
import me.kofesst.ktor.mptinformant.features.domain.repositories.ScheduleRepository
import org.jsoup.nodes.Element

class ScheduleRepositoryImpl(
    private val groupsRepository: GroupsRepository,
) : ScheduleRepository {
    companion object {
        private const val ROOT_URL = "https://mpt.ru/studentu/raspisanie-zanyatiy/"
    }

    override suspend fun getWeekLabel(): WeekLabel? = parseDocument(
        url = ROOT_URL
    ) {
        val weekLabelElement = select("span.label").first()
            ?: throw Exception("Week label span not found")
        WeekLabel.byDisplayName(weekLabelElement.text())
    }

    override suspend fun getScheduleByGroupId(groupId: String): GroupSchedule? = parseDocument(
        url = ROOT_URL
    ) {
        val tabPanelElement = select("div.tab-pane#$groupId").first()
            ?: throw Exception("Schedule tab panel not found")
        val tableElements = tabPanelElement.select("table.table")
        val scheduleDays = tableElements.map(this@ScheduleRepositoryImpl::parseScheduleDay)

        GroupSchedule(
            weekLabel = getWeekLabel() ?: WeekLabel.None,
            groupId = groupId,
            days = scheduleDays
        )
    }

    override suspend fun getScheduleByGroupName(groupName: String): GroupSchedule? {
        val group = groupsRepository.getGroupByName(groupName) ?: return null
        return getScheduleByGroupId(group.id)
    }

    private fun parseScheduleDay(tableElement: Element): GroupScheduleDay {
        val headerElement = tableElement.select("thead > tr > th > h4").first()
            ?: throw Exception("Schedule day table header not found")
        val dayOfWeek = parseScheduleDayOfWeek(headerElement)
        val branch = parseScheduleDayBranch(headerElement)
        val tableBodyElement = tableElement.select("tbody")[1]
        val tableBodyRowElements = tableBodyElement.select("tr").apply {
            removeFirst() // Remove header
            removeAll { row -> row.text().isBlank() } // Remove empty rows
        }
        val dayRows = tableBodyRowElements.map(this@ScheduleRepositoryImpl::parseScheduleRow)

        return GroupScheduleDay(
            dayOfWeek = dayOfWeek,
            branch = branch,
            rows = dayRows
        )
    }

    private fun parseScheduleDayOfWeek(headerElement: Element): DayOfWeek {
        val dayOfWeekText = headerElement.html()
            .replace(Regex("<span.*$"), "")
        return DayOfWeek.byDisplayName(dayOfWeekText)
    }

    private fun parseScheduleDayBranch(headerElement: Element): String {
        return headerElement.html()
            .replace(Regex("^.*<span.*\">"), "")
            .replace(Regex("</span>\$"), "")
    }

    private fun parseScheduleRow(tableBodyRowElement: Element): GroupScheduleRow {
        val columnElements = tableBodyRowElement.select("td")
        val lessonNumberText = columnElements[0].text()
        val lessonNumber = lessonNumberText.toIntOrNull() ?: -1
        val divided = columnElements[1].children().any { it.tagName() == "div" }
        return if (divided) {
            val lessonDivElements = columnElements[1].select("div.label")
            val teacherDivElements = columnElements[2].select("div.label")
            val numerator = GroupScheduleRow.Divided.Label(
                lesson = lessonDivElements[0].text(),
                teacher = teacherDivElements[0].text()
            )
            val denominator = GroupScheduleRow.Divided.Label(
                lesson = lessonDivElements[1].text(),
                teacher = teacherDivElements[1].text()
            )
            GroupScheduleRow.Divided(
                lessonNumber = lessonNumber,
                numerator = numerator,
                denominator = denominator
            )
        } else {
            val lesson = columnElements[1].text()
            val teacher = columnElements[2].text()
            GroupScheduleRow.Single(
                lessonNumber = lessonNumber,
                lesson = lesson,
                teacher = teacher
            )
        }
    }
}
