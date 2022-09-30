import pl.mjurek.RequestMapper
import pl.mjurek.git.GitRunner

fun main(args: Array<String>) {
    val mapper = RequestMapper()
    val request = mapper(args)
    val runner = GitRunner.of("../sample-program")
    runner.runCommandOnNumberOfCommits(request)
}
