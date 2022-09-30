package pl.mjurek.cmd

import akka.util.Helpers.isWindows
import java.io.File
import java.io.IOException

class CommandCmdExecutor private constructor(
    val workingDir: File
) {

    companion object {
        fun of(workingDir: File): CommandCmdExecutor {
            if (!workingDir.isDirectory) {
                throw IllegalArgumentException("Not a directory")
            }
            return CommandCmdExecutor(workingDir)
        }
    }

    fun runCommand(command: String): Sequence<String> {
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

    fun execute(command: String) {
        ProcessBuilder(*command.prepareCommand())
            .directory(workingDir)
            .start().waitFor()
    }

    private fun String.prepareCommand(): Array<String> {
        val program = if (isWindows()) "cmd.exe /c " else "/bin/sh -c "
        val command = program + this.trim()
        return command.split("\\s".toRegex()).toTypedArray()
    }
}
