package pl.mjurek.git

interface CommandCmdExecutor {
    fun runCommand(command: String): Sequence<String>
    fun execute(command: String)
}
