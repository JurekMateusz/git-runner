package pl.mjurek.git

import java.lang.instrument.IllegalClassFormatException

class CommitMapper {
    fun mapToDataClass(log: List<String>): Commit {
        if (log.isEmpty()) {
            throw IllegalArgumentException()
        }
        if (log.size != 4) {
            throw IllegalClassFormatException()
        }
        val commit = log[0].split(" ")[1]
        val author = log[1].split(": ")[1]
        val date = log[2].split(": ")[1].trim()
        return Commit(commit, author, date)
    }
}
