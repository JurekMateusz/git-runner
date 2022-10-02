package pl.mjurek.gitrunner.git.dto

data class TestResultDto(val status: TestStatus, val msg: String = "") {
    fun isPassed(): Boolean {
        return status == TestStatus.PASSED
    }
}
enum class TestStatus {
    PASSED, FAIL
}
