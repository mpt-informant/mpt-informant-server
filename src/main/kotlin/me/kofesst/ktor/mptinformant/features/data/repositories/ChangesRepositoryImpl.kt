package me.kofesst.ktor.mptinformant.features.data.repositories

import me.kofesst.ktor.mptinformant.features.data.utils.calendar
import me.kofesst.ktor.mptinformant.features.data.utils.getDayOfWeek
import me.kofesst.ktor.mptinformant.features.data.utils.parseDocument
import me.kofesst.ktor.mptinformant.features.data.utils.toDate
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChanges
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChangesDay
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChangesRow
import me.kofesst.ktor.mptinformant.features.domain.repositories.ChangesRepository
import me.kofesst.ktor.mptinformant.features.domain.repositories.GroupsRepository
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.*

class ChangesRepositoryImpl(
    private val groupsRepository: GroupsRepository,
) : ChangesRepository {
    companion object {
        private const val ROOT_URL = "https://mpt.ru/studentu/izmeneniya-v-raspisanii/"
        private val rowTimestampFormatter = SimpleDateFormat(
            "dd.MM.yyyy HH:mm:ss", Locale.ROOT
        )
    }

    override suspend fun getChangesByGroupId(groupId: String): GroupChanges? = parseDocument(
        url = ROOT_URL,
    ) {
        val group = groupsRepository.getGroupById(groupId)
            ?: throw Exception("Group not found")
        val groupName = group.name

        val changesDivElement = select("div.text-page div.container > div")[1]
        val changesDivChildren = changesDivElement.children().filter { element ->
            with(element.tagName()) {
                this == "h4" || (this == "div" && element.className() == "table-responsive")
            }
        }

        var date = ""
        val changesDays = mutableListOf<GroupChangesDay>()
        for (divChildElement in changesDivChildren) {
            if (divChildElement.tagName() == "h4") {
                date = divChildElement.text()
                continue
            }

            val captionElement = divChildElement.select("caption").first() ?: continue
            val groupText = captionElement.text().replace("Группа: ", "")
            if (groupText.lowercase() != groupName.lowercase()) {
                continue
            }

            changesDays.add(parseChangesDay(date, divChildElement))
        }

        GroupChanges(
            groupId = groupId,
            days = changesDays
        )
    }

    override suspend fun getChangesByGroupName(groupName: String): GroupChanges? {
        val group = groupsRepository.getGroupByName(groupName) ?: return null
        return getChangesByGroupId(group.id)
    }

    private fun parseChangesRow(tableRowElement: Element): GroupChangesRow {
        val cols = tableRowElement.children()
        val number = cols[0].text().toInt()
        val oldSubject = cols[1].text()
        val newSubject = cols[2].text()
        val timestamp = (rowTimestampFormatter.parse(cols[3].text()) ?: Date(0)).time

        return if (oldSubject.lowercase() == "дополнительное занятие") {
            GroupChangesRow.Additional(number, newSubject, timestamp)
        } else {
            GroupChangesRow.Replace(number, oldSubject, newSubject, timestamp)
        }
    }

    private fun parseChangesDay(date: String, changesDivElement: Element): GroupChangesDay {
        val tableBody = changesDivElement.select("tbody").first()
            ?: throw Exception("Table body of changes day not found")
        val tableRows = tableBody.children().apply {
            removeFirst() // Remove header
        }
        val changesRows = tableRows.map(this@ChangesRepositoryImpl::parseChangesRow)

        val dateText = date
            .replace(Regex("^(Замены на )"), "")
            .replace(Regex(" \\(.*\\)$"), "")
            .toDate(format = "dd.MM.yyyy")
        val dayOfWeek = dateText.calendar().getDayOfWeek()

        return GroupChangesDay(
            timestamp = dateText.time,
            dayOfWeek = dayOfWeek,
            rows = changesRows
        )
    }
}
