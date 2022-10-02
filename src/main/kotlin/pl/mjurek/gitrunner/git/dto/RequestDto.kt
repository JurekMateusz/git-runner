package pl.mjurek.gitrunner.git.dto

import java.util.function.Predicate

data class RequestDto(
    val numOfCommits: Int = 100, val workingDir: String, val command: String, val conditions: List<PredicateTest>
) {
    companion object FACTORY {
        private val ALLOWED_ARGS = setOf("-c", "-dir", "-e", "-ne", "-exec")

        fun of(args: Array<String>): RequestDto {
            if(args.isEmpty()) throw IllegalArgumentException("No given arguments.")
            val argsInMap = createMap(args)
            return RequestDto(
                numOfCommits = argsInMap["-c"]!![0].toInt(),
                workingDir = argsInMap["-dir"]!![0],
                command = argsInMap["-exec"]!![0],
                conditions = createConditions(argsInMap["-e"]!!)
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

        private fun createConditions(conditions: List<String>, negation: Boolean = false): List<PredicateTest> {
            return conditions.map {
                val predicate: Predicate<String> = Predicate { tested -> tested.contains(it) }
                PredicateTest(
                    predicate = if (negation) predicate.negate() else predicate, msg = it
                )
            }
        }
    }

    fun test(seqResult: List<String>): TestResultDto {
        val tested = seqResult.flatMap { line -> conditions.map { it.test(line) } }.toSet()
        return tested.find { it.isPassed() } ?: tested.find { !it.isPassed() }!!
    }
}

data class PredicateTest(
    val predicate: Predicate<String>, val msg: String
) {
    fun test(line: String): TestResultDto {
        return if (predicate.test(line)) {
            TestResultDto(TestStatus.PASSED, "Found given text: $msg")
        } else {
            TestResultDto(TestStatus.FAIL, "Not found text: $msg")
        }
    }
}
