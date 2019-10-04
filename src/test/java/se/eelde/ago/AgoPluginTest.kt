package se.eelde.ago

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

internal class AgoPluginTest {

    @get:Rule
    val testProjectDir = TemporaryFolder()

    private lateinit var buildFile: File

    @Before
    fun setup() {
        buildFile = testProjectDir.newFile("build.gradle").also {
            it.writeText("""
            plugins {
                id 'se.eelde.ago'
            }
        """)
        }
    }

    @Test
    fun `test successful result with all optimizations enabled`() {

        testProjectDir.newFile("gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
                .withEnvironment(mapOf())
                .withProjectDir(testProjectDir.root)
                .withArguments("androidGradleOptimizations")
                .withPluginClasspath()
                .build()

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.SUCCESS)
        }
    }

    @Test
    fun `test failing result with parallel buils disabled`() {

        testProjectDir.newFile("gradle.properties").writeText("""
org.gradle.parallel=false
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
                .withEnvironment(mapOf())
                .withProjectDir(testProjectDir.root)
                .withArguments("androidGradleOptimizations")
                .withPluginClasspath()
                .buildAndFail()

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.FAILED)
        }
    }

    @Test
    fun `test failing result with daemon disabled`() {

        testProjectDir.newFile("gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=false
org.gradle.configureondemand=true
org.gradle.caching=true
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
                .withEnvironment(mapOf())
                .withProjectDir(testProjectDir.root)
                .withArguments("androidGradleOptimizations")
                .withPluginClasspath()
                .build()

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.SUCCESS)
        }
    }

    @Test
    fun `test failing result with caching disabled`() {

        testProjectDir.newFile("gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=false
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
                .withEnvironment(mapOf())
                .withProjectDir(testProjectDir.root)
                .withArguments("androidGradleOptimizations")
                .withPluginClasspath()
                .buildAndFail()

        build.tasks[0].also { buildTask ->
            assertThat(buildTask.outcome).isEqualTo(TaskOutcome.FAILED)
        }
    }

    @Test
    fun `test task not added on ci`() {

        testProjectDir.newFile("gradle.properties").writeText("""
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
        """)

        @Suppress("UnstableApiUsage")
        val build = GradleRunner.create()
                .withEnvironment(mapOf("CI" to "true", "CIRCLECI" to "true"))
                .withProjectDir(testProjectDir.root)
                .withArguments("androidGradleOptimizations")
                .withPluginClasspath()
                .buildAndFail()

        assertThat(build.tasks.size).isEqualTo(0)
    }
}