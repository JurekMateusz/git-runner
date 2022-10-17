package pl.mjurek.gitrunner.git.dto

data class RequestDto(
    val numOfCommits: Int = 100, val workingDir: String, val command: String, val searchText: String
) {
    companion object FACTORY {
        private val ALLOWED_ARGS = setOf("-c", "-dir", "-f", "-exec")

        fun of(args: Array<String>): RequestDto {
            if (args.isEmpty()) throw IllegalArgumentException("No given arguments.")
            val argsInMap = createMap(args)
            return RequestDto(
                numOfCommits = argsInMap["-c"]!![0].toInt(),
                workingDir = argsInMap["-dir"]!![0],
                command = argsInMap["-exec"]!!.joinToString(separator = " "),
                searchText = (argsInMap["-f"] ?: emptyList()).joinToString(separator = " ")
            )
        }

        private fun createMap(args: Array<String>): Map<String, List<String>> {
            var map: Map<String, List<String>> = ALLOWED_ARGS.associateWith { mutableListOf() }
            var prevArg: String? = null
            for (arg in args) {
                if (prevArg == null || ALLOWED_ARGS.contains(arg)) {
                    prevArg = arg
                } else {
                    val arguments: List<String> = map[prevArg]!!
                    val plusElement = arguments.plusElement(arg)
                    map = map + Pair(prevArg, plusElement)
                }
            }
            return map
        }
    }
}
