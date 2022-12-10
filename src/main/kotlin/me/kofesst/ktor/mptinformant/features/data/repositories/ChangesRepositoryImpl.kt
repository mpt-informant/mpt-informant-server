package me.kofesst.ktor.mptinformant.features.data.repositories

import me.kofesst.ktor.mptinformant.features.data.utils.*
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChanges
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChangesDay
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChangesRow
import me.kofesst.ktor.mptinformant.features.domain.repositories.ChangesRepository
import me.kofesst.ktor.mptinformant.features.domain.repositories.GroupsRepository
import org.jsoup.nodes.Element

class ChangesRepositoryImpl(
    private val groupsRepository: GroupsRepository,
) : ChangesRepository {
    companion object {
        private const val ROOT_URL = "https://mpt.ru/studentu/izmeneniya-v-raspisanii/"
    }

    override suspend fun getGroupChanges(groupIdOrName: String): GroupChanges? = parseDocument(
        url = ROOT_URL,
    ) {
        val group = groupsRepository.getGroup(groupIdOrName)
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
            groupId = groupIdOrName,
            days = changesDays
        )
    }

    private fun parseChangesRow(tableRowElement: Element): GroupChangesRow {
        val columns = tableRowElement.children()
        val rowNumber = columns[0].text().toInt()
        val replacedString = columns[1].text()
        val replacementString = columns[2].text()
        val insertTimestampString = columns[3].text()
        return GroupChangesRow.parse(rowNumber, replacedString, replacementString, insertTimestampString)
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
