package pl.mjurek

data class RequestDto(
    val numOfCommits: Int,
    val command: String,
    val text: TextFiler
)

data class TextFiler(
    val text: String,
    val exist: Boolean = true
)
