package pl.mjurek.gitrunner.git.dto

data class GitCheckoutResultDto(val result: List<String>, val status: Status) {
    companion object Factory {
        private const val GIT_ERROR = "error:"
        fun of(seq: Sequence<String>): GitCheckoutResultDto {
            val result = seq.toList()
            val status = determineStatus(result);
            return GitCheckoutResultDto(result, status)
        }

        private fun determineStatus(result: List<String>): Status {
            val found = result.find { it.contains(GIT_ERROR) }
            return if (found == null) Status.SUCCESS else Status.FAIL
        }

    }

    fun isFail(): Boolean {
        return status == Status.FAIL
    }
}

enum class Status {
    SUCCESS, FAIL
}
