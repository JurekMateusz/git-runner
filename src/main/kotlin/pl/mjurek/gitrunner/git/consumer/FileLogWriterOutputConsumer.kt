package pl.mjurek.gitrunner.git.consumer

import mu.KotlinLogging
import pl.mjurek.gitrunner.git.dto.ProcessingUnit
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class FileLogWriterOutputConsumer : OutputConsumer {
    companion object {
        const val DIR_NAME = "logs/result/"
    }

    var log = KotlinLogging.logger { }
    var counter: Int = 1
    var previousUnit: ProcessingUnit? = null
    var file: File? = null

    init {
        val path = Path.of(DIR_NAME)
        if (!path.toFile().exists()) {
            Files.createDirectory(path)
        }
    }

    override fun consume(unit: ProcessingUnit) {
        log.info { "Processing commit: ${unit.commit}" }
        if (previousUnit == null || previousUnit?.commit?.hash != unit.commit.hash) {
            file = createNewFile(unit)
        }
        file!!.appendText(unit.getTextJoined())
        counter += 1
    }

    private fun createNewFile(unit: ProcessingUnit): File {
        val fileName = "$DIR_NAME${counter}_${unit.getShortHash()}.log"
        log.info { "Creating file $fileName" }
        val file = Path.of(fileName).toFile()
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    private fun ProcessingUnit.getTextJoined(): String {
        return commandResult.joinToString(separator = System.lineSeparator())
    }

    private fun ProcessingUnit.getShortHash(): String {
        return commit.hash.substring(5)
    }
}
