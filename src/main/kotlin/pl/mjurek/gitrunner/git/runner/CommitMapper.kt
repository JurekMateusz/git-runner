package pl.mjurek.gitrunner.git.runner

import pl.mjurek.gitrunner.git.dto.Commit
import java.lang.instrument.IllegalClassFormatException

class CommitMapper {
    fun mapToDataClass(seq: Sequence<String>): Sequence<Commit> {
        return seq.filter { isCommit(it) || isAuthor(it) || isDate(it) }
            .chunked(3)
            .map { mapToDataClass(it) }
    }

    fun mapToDataClass(log: List<String>): Commit {
        if (log.isEmpty()) {
            throw IllegalArgumentException()
        }
        if (log.size != 3) {
            throw IllegalClassFormatException()
        }
        val commit = log[0].split(" ")[1]
        val author = log[1].split(": ")[1]
        val date = log[2].split(": ")[1].trim()
        return Commit(commit, author, date)
    }

    private fun isCommit(string: String): Boolean = string.startsWith("commit ")

    private fun isAuthor(string: String): Boolean = string.contains("Author:")

    private fun isDate(string: String): Boolean = string.contains("Date:")
}
