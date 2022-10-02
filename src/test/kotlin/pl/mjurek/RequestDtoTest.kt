package pl.mjurek

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class RequestDtoTest {
    @Test
    fun `Should map request`() {
        val input = arrayOf(
            "-c",
            "4",
            "-dir",
            "../sample-program",
            "-ne",
            "javax.el:javax.el-api:jar",
            "-exec",
            "mvn dependency:tree"
        )

        val result = RequestDto.of(input)

//        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(
//            RequestDto(
//                4, "../sample-program", "mvn dependency:tree",
//                listOf(
//                    PredicateTest(
//                        predicate = { it -> it.contains("javax.el:javax.el-api:jar") },
//                        msg = "javax.el:javax.el-api:jar"
//                    )
//                )
//            )
//        )
    }
}
