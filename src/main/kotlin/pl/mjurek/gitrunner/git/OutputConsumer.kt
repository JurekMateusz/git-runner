package pl.mjurek.gitrunner.git

import pl.mjurek.gitrunner.git.dto.Commit
import pl.mjurek.gitrunner.git.dto.CommitExecutionResult
import scala.Tuple2

interface OutputConsumer {
    fun consume(output: Sequence<CommitExecutionResult>)
}
