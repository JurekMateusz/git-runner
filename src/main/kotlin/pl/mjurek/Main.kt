import pl.mjurek.RequestDto
import pl.mjurek.git.GitRunner

fun main(args: Array<String>) {
    val request = RequestDto.of(args)
    val runner = GitRunner.of(request.workingDir)
    runner.runCommandOnNumberOfCommits(request)
}
