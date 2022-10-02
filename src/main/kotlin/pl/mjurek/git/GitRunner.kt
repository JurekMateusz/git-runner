package pl.mjurek.git

import pl.mjurek.RequestDto
import pl.mjurek.TestResult
import pl.mjurek.cmd.CommandCmdExecutorImpl
import java.io.File
import java.util.*

class GitRunner private constructor(
    private val cmdExecutor: CommandCmdExecutor,
    private val mapper: CommitMapper = CommitMapper(),
    private val logFile: File
) {
    companion object {
        const val CHECKOUT: String = "git checkout"
        const val LOGS: String = "git log"
        fun of(dirPath: String): GitRunner {
            val dir = File(dirPath)
            val logs = File("logs.log")
            logs.writeText("")
            return GitRunner(
                CommandCmdExecutorImpl.of(dir),
                logFile = logs
            )
        }
    }

    fun runCommandOnNumberOfCommits(request: RequestDto) {
        val commits: Queue<Commit> = getCommits(request.numOfCommits)
        for (commit in commits) {
            val checkout = checkoutToCommit(commit)
            if (checkout.isFail()) {
                break
            }
            runCommand(commit, request)
        }
    }

    private fun writeToLogsFile(checkoutResult: GitCheckoutResult) {
        val byteArray = checkoutResult.result
            .joinToString(separator = System.lineSeparator())
            .toByteArray()
        logFile.appendBytes(byteArray)
    }

    private fun getCommits(commitsFromHead: Int): Queue<Commit> {
        val seq = cmdExecutor.runCommand(LOGS)
        return mapper.mapToDataClass(seq)
            .take(commitsFromHead)
            .toCollection(LinkedList())
    }

    private fun checkoutToCommit(commit: Commit): GitCheckoutResult {
        val gitCheckoutCommand = "$CHECKOUT ${commit.hash}"
        val seqResult = cmdExecutor.executeAndBlock(gitCheckoutCommand)
        val result = GitCheckoutResult.of(seqResult)
        writeToLogsFile(result)
        return result
    }

    val lineSep = System.lineSeparator()

    private fun runCommand(commit: Commit, request: RequestDto) {
        val result = cmdExecutor.runCommand(request.command)
            .toList()
        val textResult: TestResult? = request.test(result)
        println(
            """
            Commit: ${commit.hash} Date: ${commit.date} $lineSep
            ${textResult}
            """
        )
        logFile.appendBytes(
            result.joinToString(separator = System.lineSeparator())
                .toByteArray()
        )
    }
}
