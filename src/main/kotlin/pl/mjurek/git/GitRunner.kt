package pl.mjurek.git

import pl.mjurek.RequestDto
import pl.mjurek.cmd.CommandCmdExecutor
import java.io.File
import java.util.*

class GitRunner private constructor(
    private val cmdExecutor: CommandCmdExecutor, private val mapper: CommitMapper = CommitMapper()
) {
    companion object {
        const val CHECKOUT: String = "git checkout "
        const val LOGS: String = "git log "
        fun of(dirPath: String): GitRunner {
            val dir = File(dirPath)
            return GitRunner(CommandCmdExecutor.of(dir))
        }
    }

    fun runCommandOnNumberOfCommits(request: RequestDto) {
        val commits: Queue<Commit> = getCommits(request.numOfCommits)
        commits.forEach {
            checkoutToCommit(it)
            runCommand(it, "request.text")
        }
    }

    private fun runCommand(it: Commit, command: String) {
        println(command)
    }

    private fun checkoutToCommit(commit: Commit) {
        println(commit)
    }


    private fun getCommits(commitsFromHead: Int): Queue<Commit> {
        return cmdExecutor.runCommand(LOGS).chunked(4).map { mapper.mapToDataClass(it) }.take(commitsFromHead)
            .toCollection(LinkedList())
    }

}
