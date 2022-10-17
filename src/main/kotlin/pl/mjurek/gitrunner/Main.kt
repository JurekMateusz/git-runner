package pl.mjurek.gitrunner

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import pl.mjurek.gitrunner.git.consumer.FileLogWriterOutputConsumer
import pl.mjurek.gitrunner.git.consumer.FindTestOutputConsumer
import pl.mjurek.gitrunner.git.consumer.OutputConsumer
import pl.mjurek.gitrunner.git.dto.CommitExecutionResult
import pl.mjurek.gitrunner.git.dto.ProcessingUnit
import pl.mjurek.gitrunner.git.dto.RequestDto
import pl.mjurek.gitrunner.git.runner.GitRunner
import pl.mjurek.gitrunner.git.runner.GitRunnerRequest

const val SIZE_CHUNK = 100



fun main(args: Array<String>) {
    val request = RequestDto.of(args)
    val runner = GitRunner.of(GitRunnerRequest.of(request))

    val consumers: List<OutputConsumer> = listOf(FileLogWriterOutputConsumer(), FindTestOutputConsumer(request.searchText))
    runBlocking {
        val result: Sequence<CommitExecutionResult> = runner.execute()

        val outputChunked: Sequence<ProcessingUnit> = result.flatMap { commitResult ->
            commitResult.commandExecution.chunked(SIZE_CHUNK)
                .map { ProcessingUnit(commitResult.commit, commitResult.checkoutResult, it) }
        }
        outputChunked.flatMap { unitWork ->
            consumers.map {
                launch {
                    it.consume(unitWork)
                }
            }
        }.forEach { it.join() }
    }
}
