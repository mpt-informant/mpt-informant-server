package me.kofesst.ktor.mptinformant.features.domain.models.changes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class GroupChangesRow {
    abstract val insertTimestamp: Long

    @Serializable
    @SerialName("replace")
    data class Replace(
        @SerialName("lesson_number")
        val lessonNumber: Int,

        @SerialName("replaced_lesson")
        val replacedLesson: String,

        @SerialName("replaced_teacher")
        val replacedTeacher: String,

        @SerialName("replacement_lesson")
        val replacementLesson: String,

        @SerialName("replacement_teacher")
        val replacementTeacher: String,

        @SerialName("insert_timestamp")
        override val insertTimestamp: Long,
    ) : GroupChangesRow()

    @Serializable
    @SerialName("additional")
    data class Additional(
        @SerialName("lesson_number")
        val lessonNumber: Int,

        @SerialName("lesson")
        val lesson: String,

        @SerialName("teacher")
        val teacher: String,

        @SerialName("insert_timestamp")
        override val insertTimestamp: Long,
    ) : GroupChangesRow()

    @Serializable
    @SerialName("moved")
    data class Moved(
        @SerialName("lesson")
        val lesson: String,

        @SerialName("teacher")
        val teacher: String,

        @SerialName("moved_from")
        val movedFrom: Int,

        @SerialName("moved_to")
        val movedTo: Int,

        @SerialName("insert_timestamp")
        override val insertTimestamp: Long,
    ) : GroupChangesRow()

    @Serializable
    @SerialName("canceled")
    data class Canceled(
        @SerialName("lesson_number")
        val lessonNumber: Int,

        @SerialName("lesson")
        val lesson: String,

        @SerialName("teacher")
        val teacher: String,

        @SerialName("cause")
        val cause: CancelCause,

        @SerialName("insert_timestamp")
        override val insertTimestamp: Long,
    ) : GroupChangesRow() {
        enum class CancelCause {
            Reworked,
            DueProgram
        }
    }
}
