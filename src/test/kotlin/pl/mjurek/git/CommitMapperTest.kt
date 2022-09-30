package pl.mjurek.git

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CommitMapperTest {
    companion object {
        const val STRING_COMMIT = """commit e9ce57cfe1e2d8a5dc211aa95cbfb6396ac8439b (HEAD -> master)
        Author: Mateusz Jurek <mateuszjurek98@outlook.com>
        Date:   Fri Sep 30 16:57:52 2022 +0200
            init
    """
    }

    val mapper: CommitMapper = CommitMapper()

    @Test
    fun `Should map given string to dto`() {
        val input = STRING_COMMIT.split("\n").map { it.trim() }.dropLast(1)

        val mapped = mapper.mapToDataClass(input)

        assertThat(mapped).usingRecursiveComparison().isEqualTo(
            Commit(
                "e9ce57cfe1e2d8a5dc211aa95cbfb6396ac8439b",
                "Mateusz Jurek <mateuszjurek98@outlook.com>",
                "Fri Sep 30 16:57:52 2022 +0200"
            )
        )
    }
}
