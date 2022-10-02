package pl.mjurek.cmd

import akka.util.Helpers.isWindows
import pl.mjurek.git.CommandCmdExecutor
import java.io.File
import java.io.IOException

class CommandCmdExecutorImpl private constructor(
    val workingDir: File
) : CommandCmdExecutor {

    companion object {
        fun of(workingDir: File): CommandCmdExecutorImpl {
            if (!workingDir.isDirectory) {
                throw IllegalArgumentException("Not a directory")
            }
            return CommandCmdExecutorImpl(workingDir)
        }
    }

    override fun runCommand(command: String): Sequence<String> {
        return try {
            val proc = ProcessBuilder(*command.prepareCommand())
                .directory(workingDir)
                .start()
            val lines = proc.inputStream.bufferedReader().lines()
            Sequence { lines.iterator() }
        } catch (e: IOException) {
            e.printStackTrace()
            emptySequence()
        }
    }

    override fun executeAndBlock(command: String): Sequence<String> {
        val process = ProcessBuilder(*command.prepareCommand())
            .directory(workingDir)
            .start()
        process.waitFor()
        val output = process.inputStream.bufferedReader().lines()
        val errorLines = process.errorStream.bufferedReader().lines()
        return sequenceOf(output, errorLines)
            .flatMap { Sequence { it.iterator() } }
    }

    private fun String.prepareCommand(): Array<String> {
        val program = if (isWindows()) "cmd.exe /c " else "/bin/sh -c "
        val command = program + this.trim()
        return command.split("\\s".toRegex()).toTypedArray()
    }
}
