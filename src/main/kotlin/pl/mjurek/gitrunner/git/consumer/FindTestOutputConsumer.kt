package pl.mjurek.gitrunner.git.consumer

import pl.mjurek.gitrunner.git.dto.ProcessingUnit

class FindTestOutputConsumer(val searchText: String) : OutputConsumer {
    var previousUnit: ProcessingUnit? = null

    override fun consume(unit: ProcessingUnit) {
        if (unit.commit.hash == previousUnit?.commit?.hash) {
            return
        }
        val found = unit.commandResult.find { it.contains(searchText) }
        println(
            String.format(
                TEMPLATE,
                unit.commit.hash,
                unit.commit.date,
                """${found.getTestFound()} "$searchText"""",
                found ?: ""
            )
        )
        previousUnit = unit
    }

    companion object {
        const val TEMPLATE = """
            Commit: %s
            Date: %s
            Result: %s
            
                 %s
                
        """
    }

    private fun String?.getTestFound(): String {
        return if (this != null) "Found text: " else "Not found text: "
    }
}
