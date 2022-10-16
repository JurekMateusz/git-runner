package pl.mjurek.gitrunner.git.runner

import mu.KotlinLogging
import pl.mjurek.gitrunner.cmd.CommandCmdExecutorImpl
import pl.mjurek.gitrunner.cmd.CommandCmdExecutor
import pl.mjurek.gitrunner.git.dto.Commit
import pl.mjurek.gitrunner.git.dto.CommitExecutionResult
import pl.mjurek.gitrunner.git.dto.GitCheckoutResult
import pl.mjurek.gitrunner.git.dto.RequestDto
import java.io.File
import java.util.*

class GitRunner private constructor(
    private val cmdExecutor: CommandCmdExecutor,
    private val request: RequestDto,
    private val mapper: CommitMapper = CommitMapper(),
) {
    var log = KotlinLogging.logger { }

    companion object {
        const val CHECKOUT: String = "git checkout"
        const val LOGS: String = "git log"
        fun of(request: RequestDto): GitRunner {
            val dir = File(request.workingDir)
            return GitRunner(
                cmdExecutor = CommandCmdExecutorImpl.of(dir), request = request
            )
        }
    }

    fun execute(): Sequence<CommitExecutionResult> = sequence {
        val commits: Queue<Commit> = getCommits(request.numOfCommits)
        for (commit in commits) {
            log.info { "Checking out to commit: ${commit.hash}" }
            val checkout = checkoutToCommit(commit)
            if (checkout.isFail()) {
                log.error { "Checking out to commit: ${commit.hash} has fail" }
                yield(CommitExecutionResult(commit, checkout, emptySequence()))
                break
            }
            log.info { "Execution command: [${request.command}]" }
            val executionResult = cmdExecutor.runCommand(request.command)
            yield(CommitExecutionResult(commit, checkout, executionResult))
        }
    }

    private fun getCommits(commitsFromHead: Int): Queue<Commit> {
        val seq = cmdExecutor.runCommand(LOGS)
        return mapper.mapToDataClass(seq).take(commitsFromHead).toCollection(LinkedList())
    }

    private fun checkoutToCommit(commit: Commit): GitCheckoutResult {
        val gitCheckoutCommand = "$CHECKOUT ${commit.hash}"
        val seqResult = cmdExecutor.executeAndBlock(gitCheckoutCommand)
        return GitCheckoutResult.of(seqResult)
    }
}
