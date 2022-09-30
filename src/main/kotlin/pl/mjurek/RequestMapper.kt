package pl.mjurek

import scala.Tuple2

class RequestMapper {
    companion object {
        const val NUMBER_OF_COMMITS = "-c"
        const val TEXT_NOT_EXIST = "-ne"
        const val COMMAND = "-exec"
    }

    operator fun invoke(args: Array<String>): RequestDto {
        val split = args.filter { it.isNotBlank() }
        if (split.size % 2 != 0) {
            throw IllegalArgumentException("Incorrect number of args")
        }
        val args = split.chunked(2).map { Tuple2(it[0], it[1]) }.sortedBy { it._1 }
        return createRequest(args)
    }

    private fun createRequest(args: List<Tuple2<String, String>>): RequestDto {
        val num = args.get(NUMBER_OF_COMMITS).toInt()
        val text = args.get(TEXT_NOT_EXIST)
        val command = args.get(COMMAND)
        return RequestDto(
            num, command, TextFiler(text, false)
        )
    }

    private fun List<Tuple2<String, String>>.get(arg: String): String = this.find { it._1.equals(arg) }?._2 ?: ""
}



