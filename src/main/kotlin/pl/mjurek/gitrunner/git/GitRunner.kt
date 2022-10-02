package pl.mjurek.gitrunner.git

import pl.mjurek.gitrunner.cmd.CommandCmdExecutorImpl
import pl.mjurek.gitrunner.git.dto.CommitDto
import pl.mjurek.gitrunner.git.dto.GitCheckoutResultDto
import pl.mjurek.gitrunner.git.dto.RequestDto
import pl.mjurek.gitrunner.git.dto.TestResultDto
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
        val commits: Queue<CommitDto> = getCommits(request.numOfCommits)
        for (commit in commits) {
            val checkout = checkoutToCommit(commit)
            if (checkout.isFail()) {
                break
            }
            runCommand(commit, request)
        }
    }

    private fun writeToLogsFile(checkoutResult: GitCheckoutResultDto) {
        val byteArray = checkoutResult.result
            .joinToString(separator = System.lineSeparator())
            .toByteArray()
        logFile.appendBytes(byteArray)
    }

    private fun getCommits(commitsFromHead: Int): Queue<CommitDto> {
        val seq = cmdExecutor.runCommand(LOGS)
        return mapper.mapToDataClass(seq)
            .take(commitsFromHead)
            .toCollection(LinkedList())
    }

    private fun checkoutToCommit(commit: CommitDto): GitCheckoutResultDto {
        val gitCheckoutCommand = "$CHECKOUT ${commit.hash}"
        val seqResult = cmdExecutor.executeAndBlock(gitCheckoutCommand)
        val result = GitCheckoutResultDto.of(seqResult)
        writeToLogsFile(result)
        return result
    }

    val lineSep = System.lineSeparator()

    private fun runCommand(commit: CommitDto, request: RequestDto) {
        val result = cmdExecutor.runCommand(request.command)
            .toList()
        val textResult: TestResultDto = request.test(result)
        println(
            """
            Commit: ${commit.hash} Date: ${commit.date} $lineSep
            $textResult
            """
        )
        logFile.appendBytes(
            result.joinToString(separator = System.lineSeparator())
                .toByteArray()
        )
        logFile.appendText(lineSep + lineSep + lineSep + lineSep)
    }
}
