package se.eelde.ago

import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

internal class CiCheckerTest {

    lateinit var ciChecker: CiChecker

    @Before
    @Throws(IOException::class)
    fun setup() {
        val environmentVariables = mutableMapOf<String, String>()
        environmentVariables["CI"] = "true"
        environmentVariables["CIRCLECI"] = "true"
        environmentVariables["TRAVIS"] = "true"
        environmentVariables[""] = ""

        environmentVariables["TEAMCITY_VERSION"] = "1.0.0.0"
        environmentVariables["BUILD_NUMBER"] = "1"
        environmentVariables["BUILD_ID"] = "id:1"
        environmentVariables["BUILD_URL"] = "http://build_url/"

        ciChecker = CiChecker(environmentVariables)
    }

    @Test
    fun `test running on circle CI`() {
        assertTrue(ciChecker.isCircleCi())
    }

    @Test
    fun `test running on travis CI`() {
        assertTrue(ciChecker.isTravis())
    }

    @Test
    fun `test running on teamcity CI`() {
        assertTrue(ciChecker.isTeamCity())
    }

    @Test
    fun `test running on jenkins CI`() {
        assertTrue(ciChecker.isJenkis())
    }
}