package me.kofesst.ktor.mptinformant.features.domain.models.changes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class GroupChangesRow {
    abstract val lessonNumber: Int
    abstract val replacementLesson: String
    abstract val insertTimestamp: Long

    @Serializable
    @SerialName("replace")
    data class Replace(
        @SerialName("lesson_number")
        override val lessonNumber: Int,

        @SerialName("replaced_lesson")
        val replacedLesson: String,

        @SerialName("replacement_lesson")
        override val replacementLesson: String,

        @SerialName("insert_timestamp")
        override val insertTimestamp: Long,
    ) : GroupChangesRow()

    @Serializable
    @SerialName("additional")
    data class Additional(
        @SerialName("lesson_number")
        override val lessonNumber: Int,

        @SerialName("replacement_lesson")
        override val replacementLesson: String,

        @SerialName("insert_timestamp")
        override val insertTimestamp: Long,
    ) : GroupChangesRow()
}
