package pl.mjurek

import akka.util.Helpers.isWindows
import pl.mjurek.util.loggerFor
import java.io.File
import java.io.IOException
import java.util.stream.Stream

class CommandCmdExecutor private constructor(
    val workingDir: File
) {
    private val LOG = loggerFor(javaClass)

    companion object {
        fun of(workingDir: File): CommandCmdExecutor {
            if (!workingDir.isDirectory) {
                throw IllegalArgumentException("Not a directory")
            }
            return CommandCmdExecutor(workingDir)
        }
    }

    fun runCommand(command: String): Stream<String> {
        return try {
            val proc = ProcessBuilder(*command.prepareCommand())
                .directory(workingDir)
                .start()
            proc.inputStream.bufferedReader().lines()
        } catch (e: IOException) {
            e.printStackTrace()
            Stream.of()
        }
    }

    private fun String.prepareCommand(): Array<String> {
        val program = if (isWindows()) "cmd.exe /c " else "/bin/sh -c "
        val command = program + this.trim()
        return command.split("\\s".toRegex()).toTypedArray()
    }
}
