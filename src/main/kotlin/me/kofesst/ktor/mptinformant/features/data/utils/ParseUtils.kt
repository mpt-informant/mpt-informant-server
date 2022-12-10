package me.kofesst.ktor.mptinformant.features.data.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChangesRow
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat
import java.util.*

suspend inline fun <T : Any> parseDocument(
    url: String,
    crossinline block: suspend Document.() -> T?,
): T? = withContext(Dispatchers.IO) {
    runCatching {
        val document = Jsoup.connect(url).get()
        block(document)
    }.getOrNull()
}

suspend inline fun <T : Any> parseDocument(
    url: String,
    defaultValue: T,
    crossinline block: suspend Document.() -> T,
): T = withContext(Dispatchers.IO) {
    runCatching {
        val document = Jsoup.connect(url).get()
        block(document)
    }.getOrDefault(defaultValue)
}

private val teacherRegex = Regex("([А-Я]\\.){2} [А-Яа-я]+")
private val lessonRegex = Regex(".*(?=([А-Я]\\.){2} [А-Яа-я]+)")
fun String.getLessonAndTeachers(): Pair<String, String> {
    val isComplex = this.contains("(занятие проводится)", ignoreCase = true)
    val lesson = if (isComplex) {
        this
    } else {
        lessonRegex.find(this)?.value ?: this
    }
    val teachers = if (lesson.isNotBlank()) {
        teacherRegex.findAll(this).joinToString(", ") { it.value }
    } else {
        ""
    }
    return lesson.trim() to teachers.trim()
}

val changesTimestampFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ROOT)
fun GroupChangesRow.Companion.parse(
    rowNumber: Int,
    replacedString: String,
    replacementString: String,
    insertTimestampString: String,
): GroupChangesRow {
    val replacedData = replacedString.getLessonAndTeachers()
    val replacementData = replacementString.getLessonAndTeachers()
    val insertTimestamp = (changesTimestampFormat.parse(insertTimestampString) ?: Date(0)).time

    val isAdditional = replacedString.contains("дополнительное занятие", ignoreCase = true)
    val isMoved = replacementString.contains("занятие перенесено", ignoreCase = true)
    val isCanceled = replacementString.contains("занятие отменено", ignoreCase = true)
    return when {
        isAdditional -> {
            GroupChangesRow.Additional(
                lessonNumber = rowNumber,
                lesson = replacementData.first,
                teacher = replacementData.second,
                insertTimestamp = insertTimestamp
            )
        }

        isMoved -> {
            val movedTo = Regex("\\d").find(replacementString)?.value?.toIntOrNull() ?: -1
            GroupChangesRow.Moved(
                lesson = replacedData.first,
                teacher = replacedData.second,
                movedFrom = rowNumber,
                movedTo = movedTo,
                insertTimestamp = insertTimestamp
            )
        }

        isCanceled -> {
            GroupChangesRow.Canceled(
                lessonNumber = rowNumber,
                lesson = replacedData.first,
                teacher = replacedData.second,
                cause = when {
                    replacementString.contains("с последующей отработкой", ignoreCase = true) -> {
                        GroupChangesRow.Canceled.CancelCause.Reworked
                    }

                    else -> {
                        GroupChangesRow.Canceled.CancelCause.DueProgram
                    }
                },
                insertTimestamp = insertTimestamp
            )
        }

        else -> {
            GroupChangesRow.Replace(
                lessonNumber = rowNumber,
                replacedLesson = replacedData.first,
                replacedTeacher = replacedData.second,
                replacementLesson = replacementData.first,
                replacementTeacher = replacementData.second,
                insertTimestamp = insertTimestamp
            )
        }
    }
}
