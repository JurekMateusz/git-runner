package pl.mjurek.gitrunner

import pl.mjurek.gitrunner.git.FileLogWriterOutputConsumer
import pl.mjurek.gitrunner.git.runner.GitRunner
import pl.mjurek.gitrunner.git.dto.Commit
import pl.mjurek.gitrunner.git.dto.CommitExecutionResult
import pl.mjurek.gitrunner.git.dto.RequestDto
import scala.Tuple2


fun main(args: Array<String>) {
    val request = RequestDto.of(args)
    val runner = GitRunner.of(request)
    val result: Sequence<CommitExecutionResult> = runner.execute()

    val fileLogWriter = FileLogWriterOutputConsumer()
    fileLogWriter.consume(result)


}
