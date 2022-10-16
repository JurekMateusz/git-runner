package pl.mjurek.gitrunner.git.dto

data class ProcessingUnit(
    val commit: Commit,
    val checkoutResult: GitCheckoutResult,
    val commandResult: List<String>
)

