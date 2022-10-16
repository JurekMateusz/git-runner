package pl.mjurek.gitrunner.cmd

interface CommandCmdExecutor {
    fun runCommand(command: String): Sequence<String>
    fun executeAndBlock(command: String): Sequence<String>
}
