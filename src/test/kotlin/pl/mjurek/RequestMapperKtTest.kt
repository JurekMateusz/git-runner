package pl.mjurek

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RequestMapperKtTest {

    @Test
    fun `Should map request`() {
        val input = arrayOf("-c", "4", "-ne", "javax.el:javax.el-api:jar", "-exec", "mvn dependency:tree")

        val result = RequestMapper()(input)

        assertThat(result).usingRecursiveComparison().isEqualTo(
            RequestDto(
                4, "mvn dependency:tree",
                TextFiler("javax.el:javax.el-api:jar", false)
            )
        )
    }
}
