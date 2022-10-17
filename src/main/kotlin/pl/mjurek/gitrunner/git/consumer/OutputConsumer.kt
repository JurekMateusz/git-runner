package pl.mjurek.gitrunner.git.consumer

import pl.mjurek.gitrunner.git.dto.ProcessingUnit

interface OutputConsumer {
    fun consume(unit: ProcessingUnit)
}
