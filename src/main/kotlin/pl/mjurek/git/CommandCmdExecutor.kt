package pl.mjurek.git

interface CommandCmdExecutor {
    fun runCommand(command: String): Sequence<String>
    fun executeAndBlock(command: String): Sequence<String>
}
