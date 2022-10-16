package pl.mjurek.git

import akka.util.Helpers
import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pl.mjurek.gitrunner.git.dto.RequestDto
import pl.mjurek.gitrunner.git.runner.GitRunner
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.appendText


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GitRunnerTest {

    companion object {
        const val DIR = "git-runner-tested-repository"
        const val FILE = "file-versioned.txt"
    }

    lateinit var testedDir: File
    var log = KotlinLogging.logger { }

    @BeforeAll
    fun `Set up tested git repository`() {
        testedDir = Files.createTempDirectory(DIR).toFile()
        runCommand("git init .", testedDir)
        val versionedFile = Files.createFile(Path.of("${testedDir.absolutePath}\\$FILE"))
        runCommand("git add .", testedDir)
        runCommand("git commit -am \"init\"", testedDir)
        for (i in 1..3) {
            versionedFile.appendText("not not $i ${System.lineSeparator()}")
            runCommand("git commit -am \"Commit nr. $i\"", testedDir)
        }
        for (i in 4..7) {
            versionedFile.appendText("Test searched string $i ${System.lineSeparator()}")
            runCommand("git commit -am \"Commit nr. $i\"", testedDir)
        }
    }

    @Test
    fun `Should execute windows cmd reading command`() {
        val cmd = "-c 4 -dir ${testedDir.absolutePath} -exec ${Commands.GET_CONTENT} $FILE"
        val request = RequestDto.of(
            cmd.split(" ")
        )
        val gitRunner = GitRunner.of(request)

        val result = gitRunner.execute().toList()

        assertThat(result).hasSize(4)
        assertThat(result[0].commandExecution.last()).startsWith("Test searched string 7")
    }

    @AfterAll
    fun `Clean up`() {
        testedDir.deleteRecursively()
    }

    private fun runCommand(command: String, path: File) {
        val process = ProcessBuilder(*command.prepareCommand()).directory(path).start()
        process.waitFor()
        val output = process.inputStream.bufferedReader().lines()
        val errorLines = process.errorStream.bufferedReader().lines()
        val completeOutput = sequenceOf(output, errorLines).flatMap { Sequence { it.iterator() } }
        val result = completeOutput.joinToString(separator = System.lineSeparator())
        log.debug { result }
    }

    private fun String.prepareCommand(): Array<String> {
        val program = if (Helpers.isWindows()) "cmd.exe /c " else "/bin/sh -c "
        val command = program + this.trim()
        return command.split("\\s".toRegex()).toTypedArray()
    }
}
