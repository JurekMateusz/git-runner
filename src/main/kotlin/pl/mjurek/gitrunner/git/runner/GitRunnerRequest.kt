package pl.mjurek.gitrunner.git.runner

import pl.mjurek.gitrunner.git.dto.RequestDto

data class GitRunnerRequest(
    val numOfCommits: Int, val workingDir: String, val command: String
) {
    companion object {
        fun of(mapped: RequestDto): GitRunnerRequest {
            return GitRunnerRequest(mapped.numOfCommits, mapped.workingDir, mapped.command)
        }
    }
}
