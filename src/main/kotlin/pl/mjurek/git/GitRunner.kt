package pl.mjurek.git

import pl.mjurek.RequestDto
import pl.mjurek.cmd.CommandCmdExecutorImpl
import java.io.File
import java.util.*

class GitRunner private constructor(
    private val cmdExecutor: CommandCmdExecutor, private val mapper: CommitMapper = CommitMapper()
) {
    companion object {
        const val CHECKOUT: String = "git checkout"
        const val LOGS: String = "git log"
        fun of(dirPath: String): GitRunner {
            val dir = File(dirPath)
            return GitRunner(CommandCmdExecutorImpl.of(dir))
        }
    }

    fun runCommandOnNumberOfCommits(request: RequestDto) {
        val commits: Queue<Commit> = getCommits(request.numOfCommits)
        commits.forEach {
            checkoutToCommit(it)
            runCommand(it, request.command)
        }
    }

    private fun getCommits(commitsFromHead: Int): Queue<Commit> {
        val seq = cmdExecutor.runCommand(LOGS)
        return mapper.mapToDataClass(seq)
            .take(commitsFromHead)
            .toCollection(LinkedList())
    }

    private fun checkoutToCommit(commit: Commit) {
        val gitCheckoutCommand = "$CHECKOUT ${commit.hash}"
        cmdExecutor.execute(gitCheckoutCommand)
    }


    private fun runCommand(it: Commit, command: String) {
        println(command)
    }
}
