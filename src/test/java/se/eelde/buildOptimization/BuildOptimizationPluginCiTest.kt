package se.eelde.buildOptimization

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class BuildOptimizationPluginCiTest {

    @TempDir
    lateinit var testProjectDir: File

    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        buildFile = File(testProjectDir, "build.gradle").also {
            it.writeText(
                """
            plugins {
                id 'se.eelde.build_optimization'
            }
        """
            )
        }
    }

    @Test
    fun `test task not added on circle`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=false
org.gradle.daemon=false
org.gradle.configureondemand=false
org.gradle.caching=false
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withEnvironment(mapOf("CI" to "true", "CIRCLECI" to "true"))
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .buildAndFail()

        assertTrue(
            build.output.contains("Build running on Circle Ci - ignoring gradle optimization-checks."),
            build.output
        )

        assertThat(build.tasks.size).isEqualTo(0)
    }

    @Test
    fun `test task not added on travis`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=false
org.gradle.daemon=false
org.gradle.configureondemand=false
org.gradle.caching=false
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withEnvironment(mapOf("CI" to "true", "TRAVIS" to "true"))
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .buildAndFail()

        assertTrue(
            build.output.contains("Build running on Travis - ignoring gradle optimization-checks."),
            build.output
        )

        assertThat(build.tasks.size).isEqualTo(0)
    }

    @Test
    fun `test task not added on teamcity`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=false
org.gradle.daemon=false
org.gradle.configureondemand=false
org.gradle.caching=false
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withEnvironment(mapOf("TEAMCITY_VERSION" to "1.0.0.0"))
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .buildAndFail()

        assertTrue(
            build.output.contains("Build running on TeamCity - ignoring gradle optimization-checks."),
            build.output
        )

        assertThat(build.tasks.size).isEqualTo(0)
    }

    @Test
    fun `test task not added on jenkins`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=false
org.gradle.daemon=false
org.gradle.configureondemand=false
org.gradle.caching=false
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withEnvironment(
                mapOf(
                    "BUILD_NUMBER" to "1",
                    "BUILD_ID" to "id:1",
                    "BUILD_URL" to "http://build_url/"
                )
            )
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .buildAndFail()

        assertTrue(
            build.output.contains("Build running on Jenkins - ignoring gradle optimization-checks."),
            build.output
        )

        assertThat(build.tasks.size).isEqualTo(0)
    }

    @Test
    fun `test task not added on github actions`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=false
org.gradle.daemon=false
org.gradle.configureondemand=false
org.gradle.caching=false
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withEnvironment(
                mapOf(
                    "CI" to "true",
                    "GITHUB_ACTION" to "globallyUnique"
                )
            )
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .buildAndFail()

        assertTrue(
            build.output.contains("Build running on Github actions - ignoring gradle optimization-checks."),
            build.output
        )

        assertThat(build.tasks.size).isEqualTo(0)
    }

    @Test
    fun `test task not added on ci`() {

        File(testProjectDir, "gradle.properties").writeText(
            """
org.gradle.parallel=false
org.gradle.daemon=false
org.gradle.configureondemand=false
org.gradle.caching=false
        """
        )

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
            .withEnvironment(
                mapOf(
                    "CI" to "true"
                )
            )
            .withProjectDir(testProjectDir)
            .withArguments("checkBuildOptimizations")
            .withPluginClasspath()
            .buildAndFail()

        assertTrue(build.output.contains("Build running on CI - ignoring gradle optimization-checks."), build.output)

        assertThat(build.tasks.size).isEqualTo(0)
    }
}
