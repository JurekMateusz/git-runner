package pl.mjurek.gitrunner.git.dto

data class CommitExecutionResult(
    val commit: Commit,
    val checkoutResult: GitCheckoutResult,
    val commandExecution: Sequence<String>
)
