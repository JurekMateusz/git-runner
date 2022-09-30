import java.io.File
import java.io.IOException
import java.util.stream.Stream

fun main(args: Array<String>) {
    val workingDir = File("../sample-program")
    val runCommand = "cmd.exe /c mvn clean test".runCommand(workingDir)
    val result = runCommand.dropWhile { it.startsWith("Results :") }
        .toList()
        .joinToString(System.lineSeparator())
    println(result)


}
