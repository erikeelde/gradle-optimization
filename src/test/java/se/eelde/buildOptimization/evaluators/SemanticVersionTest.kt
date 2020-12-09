package se.eelde.buildOptimization.evaluators

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SemanticVersionTest {

    @Test
    fun `test regexp`() {
        val versionRegexp =
            """^(?<major>0|[1-9]\d*)\.(?<minor>0|[1-9]\d*)\.?(?<patch>0|[1-9]\d*)?(?:-(?<prerelease>(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\.(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\+(?<buildmetadata>[0-9a-zA-Z-]+(?:\.[0-9a-zA-Z-]+)*))?${'$'}""".toRegex()

        versionRegexp.find("5.6.7")!!.apply {
            val major: Int? = groups["major"]!!.value.toIntOrNull()
            val minor: Int? = groups["minor"]?.value?.toIntOrNull()
            val patch: Int? = groups["patch"]?.value?.toIntOrNull()

            assertEquals(5, major)
            assertEquals(6, minor)
            assertEquals(7, patch)
            assertEquals(6, groups.size)
        }

        versionRegexp.find("5.6")!!.apply {

            val major: Int? = groups["major"]!!.value.toIntOrNull()
            val minor: Int? = groups["minor"]?.value?.toIntOrNull()
            val patch: Int? = groups["patch"]?.value?.toIntOrNull()

            assertEquals(5, major)
            assertEquals(6, minor)
            assertNull(patch)
            assertEquals(6, groups.size)
        }
    }

    @Test
    fun `test parse`() {
        SemanticVersion.parse("6.6").apply {
            assertEquals(6, major)
            assertEquals(6, minor)
            assertEquals(0, patch)
        }
        SemanticVersion.parse("6.7").apply {
            assertEquals(6, major)
            assertEquals(7, minor)
            assertEquals(0, patch)
        }
        SemanticVersion.parse("6.8").apply {
            assertEquals(6, major)
            assertEquals(8, minor)
            assertEquals(0, patch)
        }
        SemanticVersion.parse("6.7.1").apply {
            assertEquals(6, major)
            assertEquals(7, minor)
            assertEquals(1, patch)
        }
        SemanticVersion.parse("6.8.1").apply {
            assertEquals(6, major)
            assertEquals(8, minor)
            assertEquals(1, patch)
        }
    }

    @Test
    fun `test compatibilityt`() {
        val v6_7 = SemanticVersion.parse("6.7")
        val v6_6 = SemanticVersion.parse("6.6")
        val v6_7_1 = SemanticVersion.parse("6.7")
        val v6_6_1 = SemanticVersion.parse("6.6.1")
        val v6_8 = SemanticVersion.parse("6.8")
        val v6_8_1 = SemanticVersion.parse("6.8.1")

        val v6_4_1 = SemanticVersion.parse("6.4.1")
        val v7_0 = SemanticVersion.parse("7.0")

        assertTrue(v6_7.compatibleWith(v6_8))
        assertTrue(v6_7.compatibleWith(v6_7_1))
        assertTrue(v6_7.compatibleWith(v6_8))
        assertTrue(v6_7.compatibleWith(v6_8_1))
        assertFalse(v6_7.compatibleWith(v6_6))
        assertFalse(v6_7.compatibleWith(v6_6_1))
        assertFalse(v6_7.compatibleWith(v6_4_1))
        assertFalse(v6_7.compatibleWith(v7_0))
    }
}
