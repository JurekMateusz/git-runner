package pl.mjurek.gitrunner

import pl.mjurek.gitrunner.git.FileLogWriterOutputConsumer
import pl.mjurek.gitrunner.git.runner.GitRunner
import pl.mjurek.gitrunner.git.dto.CommitExecutionResult
import pl.mjurek.gitrunner.git.runner.GitRunnerRequest
import pl.mjurek.gitrunner.git.dto.RequestDto


fun main(args: Array<String>) {
    val request = RequestDto.of(args)
    val runner = GitRunner.of(GitRunnerRequest.of(request))
    val result: Sequence<CommitExecutionResult> = runner.execute()

    val fileLogWriter = FileLogWriterOutputConsumer()
    fileLogWriter.consume(result)


}
