package pl.mjurek.gitrunner.git.dto

data class GitCheckoutResult(val result: List<String>, val status: Status) {
    companion object Factory {
        private const val GIT_ERROR = "error:"
        fun of(seq: Sequence<String>): GitCheckoutResult {
            val result = seq.toList()
            val status = determineStatus(result);
            return GitCheckoutResult(result, status)
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
