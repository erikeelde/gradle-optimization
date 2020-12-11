package se.eelde.buildOptimization

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CiCheckerTest {

    @Test
    fun `test running on circle CI`() {
        val ciChecker = CiChecker(mapOf("CI" to "true", "CIRCLECI" to "true"))

        assertEquals(CiChecker.DetektedCi.Circle, ciChecker.detectedCi())
    }

    @Test
    fun `test running on travis CI`() {
        val ciChecker = CiChecker(mapOf("CI" to "true", "TRAVIS" to "true"))

        assertEquals(CiChecker.DetektedCi.Travis, ciChecker.detectedCi())
    }

    @Test
    fun `test running on teamcity CI`() {
        val ciChecker = CiChecker(mapOf("TEAMCITY_VERSION" to "1.0.0.0"))

        assertEquals(CiChecker.DetektedCi.TeamCity, ciChecker.detectedCi())
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

        assertEquals(CiChecker.DetektedCi.Jenkins, ciChecker.detectedCi())
    }

    @Test
    fun `test running on github actions`() {
        val ciChecker = CiChecker(
            mapOf(
                "CI" to "true",
                "GITHUB_ACTION" to "globallyUnique"
            )
        )

        assertEquals(CiChecker.DetektedCi.GithubActions, ciChecker.detectedCi())
    }

    @Test
    fun `test running on ci`() {
        val ciChecker = CiChecker(
            mapOf(
                "CI" to "true"
            )
        )

        assertEquals(CiChecker.DetektedCi.Generic, ciChecker.detectedCi())
    }
}
