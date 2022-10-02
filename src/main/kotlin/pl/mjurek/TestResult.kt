package pl.mjurek

data class TestResult(val status: TestStatus, val msg: String = "") {
    fun isPassed(): Boolean {
        return status == TestStatus.PASSED
    }
}
enum class TestStatus {
    PASSED, FAIL
}
