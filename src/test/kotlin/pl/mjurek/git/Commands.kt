package pl.mjurek.git

import akka.util.Helpers

class Commands {
    companion object {
        val GET_CONTENT: String = if (Helpers.isWindows()) "type" else "cat"
    }
}
