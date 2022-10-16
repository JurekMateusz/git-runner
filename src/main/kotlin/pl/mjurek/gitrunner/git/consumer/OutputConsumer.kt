package pl.mjurek.gitrunner.git.consumer

import pl.mjurek.gitrunner.git.dto.Commit
import pl.mjurek.gitrunner.git.dto.CommitExecutionResult
import pl.mjurek.gitrunner.git.dto.ProcessingUnit
import scala.Tuple2

interface OutputConsumer {
    fun consume(output: ProcessingUnit)
}
