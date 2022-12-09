package me.kofesst.ktor

import me.kofesst.ktor.mptinformant.features.data.utils.parse
import me.kofesst.ktor.mptinformant.features.domain.models.changes.GroupChangesRow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ChangesParsingTests {
    companion object {
        @JvmStatic
        fun rowTypeParams(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "Дополнительное занятие",
                "Тестовый предмет А.Б. Вгдейкин",
                GroupChangesRow.Additional::class.java
            ),
            Arguments.of(
                "Тестовый предмет А.Б. Вгдейкин",
                "Занятие перенесено на 100 пару",
                GroupChangesRow.Moved::class.java
            ),
            Arguments.of(
                "Тестовый предмет А.Б. Вгдейкин",
                "Занятие отменено в связи с выполнением программы",
                GroupChangesRow.Canceled::class.java
            ),
            Arguments.of(
                "Тестовый предмет А.Б. Вгдейкин",
                "Занятие отменено с последующей отработкой",
                GroupChangesRow.Canceled::class.java
            ),
            Arguments.of(
                "Тестовый предмет А.Б. Вгдейкин",
                "Тестовый предмет А.Б. Вгдейкин",
                GroupChangesRow.Replace::class.java
            ),
        )

        @JvmStatic
        fun canceledRowParams(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "Занятие отменено с последующей отработкой",
                GroupChangesRow.Canceled.CancelCause.Reworked
            ),
            Arguments.of(
                "Занятие отменено в связи с выполнением программы",
                GroupChangesRow.Canceled.CancelCause.DueProgram
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("rowTypeParams")
    fun `Parsed changes row type parsed as expected`(
        replacedString: String,
        replacementString: String,
        expectedClass: Class<GroupChangesRow>,
    ) {
        val parsed = GroupChangesRow.parse(
            rowNumber = -1,
            replacedString = replacedString,
            replacementString = replacementString,
            insertTimestampString = "01.01.2000 00:00:00"
        )
        assertEquals(expectedClass, parsed.javaClass, "Классы не совпадают")
    }

    @ParameterizedTest
    @MethodSource("canceledRowParams")
    fun `Canceled row cancel causes parsed as expected`(
        replacementString: String,
        expectedCause: GroupChangesRow.Canceled.CancelCause,
    ) {
        val parsed = GroupChangesRow.parse(
            rowNumber = -1,
            replacedString = "",
            replacementString = replacementString,
            insertTimestampString = "01.01.2000 00:00:00"
        ) as GroupChangesRow.Canceled
        assertEquals(expectedCause, parsed.cause, "Классы не совпадают")
    }
}
