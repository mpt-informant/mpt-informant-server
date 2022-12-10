package me.kofesst.ktor.mptinformant.features.data.repositories

import me.kofesst.ktor.mptinformant.features.data.utils.parseDocument
import me.kofesst.ktor.mptinformant.features.domain.models.Department
import me.kofesst.ktor.mptinformant.features.domain.models.Group
import me.kofesst.ktor.mptinformant.features.domain.repositories.DepartmentsRepository
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class DepartmentsRepositoryImpl : DepartmentsRepository {
    companion object {
        private const val ROOT_URL = "https://mpt.ru/studentu/raspisanie-zanyatiy/"
    }

    override suspend fun getDepartments(): List<Department> = parseDocument(
        url = ROOT_URL,
        defaultValue = emptyList()
    ) {
        val departmentsUlElement = select("ul.nav.nav-tabs").first()
            ?: throw Exception("Departments uls not found")
        val departmentElements = departmentsUlElement.children()

        parseDepartments(departmentElements)
    }

    private fun Document.parseDepartments(departmentElements: Elements): List<Department> {
        return departmentElements.mapNotNull { departmentLiElement ->
            this@parseDepartments.parseDepartment(departmentLiElement)
        }
    }

    private fun Document.parseDepartment(liElement: Element): Department? {
        val linkElement = liElement.children().first() ?: return null
        val departmentId = linkElement
            .attr("href")
            .replaceFirst("#", "")
        val departmentName = linkElement.text()
        val groupsTabPaneElement = select("div.tab-pane#$departmentId")
        val groupsUlElement = groupsTabPaneElement.select("ul.nav.nav-tabs").first() ?: return null
        val groupElements = groupsUlElement.children()
        val groups = groupElements.map { groupLiElement ->
            parseGroup(departmentId, groupLiElement)
        }

        return Department(
            id = departmentId,
            name = departmentName,
            groups = groups
        )
    }

    private fun parseGroup(departmentId: String, liElement: Element): Group {
        val linkElement = liElement.children().first() ?: throw Exception()
        val groupId = linkElement.attr("href")
            .replaceFirst("#", "")
        val groupName = linkElement.text()

        return Group(
            id = groupId,
            name = groupName,
            departmentId = departmentId
        )
    }

    override suspend fun getDepartment(idOrName: String): Department? =
        getDepartments().firstOrNull { department ->
            department.id == idOrName || department.name.contains(idOrName, ignoreCase = true)
        }
}
