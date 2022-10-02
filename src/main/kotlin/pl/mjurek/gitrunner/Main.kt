package pl.mjurek.gitrunner

import pl.mjurek.gitrunner.git.dto.RequestDto
import pl.mjurek.gitrunner.git.GitRunner

fun main(args: Array<String>) {
    val request = RequestDto.of(args)
    val runner = GitRunner.of(request.workingDir)
    runner.runCommandOnNumberOfCommits(request)
}
