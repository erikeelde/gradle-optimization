package se.eelde.ago

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class AgoPluginExtensionTest {

    @Test
    fun `test should skip optimizations parsing`() {
        val agoPluginExtension = AgoPluginExtension()
        agoPluginExtension.skipOptimizationsEnvVar = "SKIP_IT"

        assertThat(agoPluginExtension.shouldSkipOptimizations(mapOf()))
                .isFalse()

        assertThat(agoPluginExtension.shouldSkipOptimizations(mapOf("SKIP_IT" to "")))
                .isFalse()

        assertThat(agoPluginExtension.shouldSkipOptimizations(mapOf("SKIP_IT" to "true")))
                .isTrue()

        assertThat(agoPluginExtension.shouldSkipOptimizations(mapOf("SKIP_IT" to "false")))
                .isFalse()
    }
}