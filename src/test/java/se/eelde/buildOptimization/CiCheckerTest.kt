package se.eelde.buildOptimization

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class CiCheckerTest {

    @Test
    fun `test running on circle CI`() {
        val ciChecker = CiChecker(mapOf("CI" to "true", "CIRCLECI" to "true"))

        assertTrue(ciChecker.isCircleCi())
    }

    @Test
    fun `test running on travis CI`() {
        val ciChecker = CiChecker(mapOf("CI" to "true", "TRAVIS" to "true"))

        assertTrue(ciChecker.isTravis())
    }

    @Test
    fun `test running on teamcity CI`() {
        val ciChecker = CiChecker(mapOf("TEAMCITY_VERSION" to "1.0.0.0"))

        assertTrue(ciChecker.isTeamCity())
    }

    @Test
    fun `test running on jenkins CI`() {
        val ciChecker = CiChecker(
            mapOf(
                "BUILD_NUMBER" to "1",
                "BUILD_ID" to "id:1",
                "BUILD_URL" to "http://build_url/"
            )
        )

        assertTrue(ciChecker.isJenkis())
    }
}
